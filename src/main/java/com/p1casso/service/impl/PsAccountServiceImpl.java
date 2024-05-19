package com.p1casso.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.p1casso.Utils.HttpUtils;
import com.p1casso.entity.PsAccount;
import com.p1casso.entity.PsGameTrophy;
import com.p1casso.entity.PsTrophy;
import com.p1casso.enums.ConsoleEnum;
import com.p1casso.exception.P1cassoException;
import com.p1casso.mapper.PsAccountMapper;
import com.p1casso.service.PsAccountService;
import com.p1casso.vo.Trophies;
import com.p1casso.vo.TrophyGroup;
import com.p1casso.vo.TrophyGroups;
import com.p1casso.vo.TrophyTitles;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.p1casso.enums.ConsoleEnum.PS4_AND_OTHERS;
import static com.p1casso.enums.ConsoleEnum.PS5;
import static com.p1casso.enums.PSUrlEnum.*;

/**
 * @author p1casso
 * @description 针对表【ps_account】的数据库操作Service实现
 * @createDate 2023-12-31 12:33:41
 */

@Slf4j
@Service
public class PsAccountServiceImpl extends ServiceImpl<PsAccountMapper, PsAccount> implements PsAccountService {

    @Resource
    private PsGameTrophyServiceImpl psGameTrophyService;

    @Override
    public Map<String, String> login() {
        //首先判断数据库的token是否存在/过期，如果有效，则直接返回
        PsAccount account = super.getOne(null);
        Map<String, String> header = new HashMap<>();
        header.put("Accept-Language", "zh-Hans");
        if (StrUtil.isNotBlank(account.getToken()) && DateUtil.compare(new Date(), account.getTokenExpirationTime()) < 0) {
            header.put("Authorization", "Basic " + account.getToken());
        } else if (StrUtil.isNotBlank(account.getRefreshToken()) && DateUtil.compare(new Date(), account.getRefreshTokenExpirationTime()) < 0) {
            //token过期或者不存在，看refreshToken是否存在。有效
            header.put("Authorization", "Basic " + refreshToken());
        } else {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(PRE_AUTHORIZATION.getUrl()).addHeader("Cookie", "npsso=" + account.getNpsso()).build();
            String code;
            try (Response response = client.newCall(request).execute()) {
                // 获取响应中的 Location 标头
                String location = response.header("Location");
                if (location == null) {
                    throw new P1cassoException("无法获取预授权code，请更新npsso");
                }
                code = extractCodeValue(location);
                if (code == null) {
                    throw new P1cassoException("无法获code");
                }
            } catch (IOException e) {
                throw new P1cassoException("请求失败");
            }
            //获取access token
            String url = "https://ca.account.sony.com/api/authz/v3/oauth/token";
            header.put("Cookie", "npsso=" + account.getNpsso());
            header.put("Authorization", "Basic MDk1MTUxNTktNzIzNy00MzcwLTliNDAtMzgwNmU2N2MwODkxOnVjUGprYTV0bnRCMktxc1A=");
            header.put("User-Agent", "PlayStation/21090100 CFNetwork/1126 Darwin/19.5.0");
            header.put("Content-Type", "application/x-www-form-urlencoded");

            FormBody.Builder body = new FormBody.Builder();
            body.add("code", code);
            body.add("redirect_uri", "com.scee.psxandroid.scecompcall://redirect");
            body.add("grant_type", "authorization_code");
            body.add("token_format", "jwt");

            String responseString = HttpUtils.okHttpPost(url, header, body);
            Gson gson = new Gson();
            JsonElement jsonElement = gson.fromJson(responseString, JsonElement.class);
            String accessToken;
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (jsonObject.has("access_token")) {
                    accessToken = jsonObject.get("access_token").getAsString();
                    UpdateWrapper<PsAccount> psAccountUpdateWrapper = new UpdateWrapper<>();
                    psAccountUpdateWrapper.set("token", accessToken);
                    Date tokenExpirationTime = DateUtil.offset(new Date(), DateField.SECOND, 3540);
                    psAccountUpdateWrapper.set("token_expiration_time", tokenExpirationTime);
                    psAccountUpdateWrapper.set("refresh_token", jsonObject.get("refresh_token").getAsString());
                    int refreshTokenExpiresIn = jsonObject.get("refresh_token_expires_in").getAsInt();
                    Date refreshTokenExpirationTime = DateUtil.offset(new Date(), DateField.SECOND, refreshTokenExpiresIn);
                    psAccountUpdateWrapper.set("refresh_token_expiration_time", refreshTokenExpirationTime);
                    baseMapper.update(null, psAccountUpdateWrapper);
                    header.put("Authorization", "Basic " + accessToken);
                    return header;
                } else {
                    throw new P1cassoException(responseString);
                }
            } else {
                throw new P1cassoException(responseString);
            }
        }
        return header;
    }

//    private void updateAccountDetails(PsAccount account, String accessToken, JsonObject jsonObject) {
//        UpdateWrapper<PsAccount> psAccountUpdateWrapper = new UpdateWrapper<>();
//        psAccountUpdateWrapper.set("token", accessToken);
//        Date tokenExpirationTime = DateUtil.offset(new Date(), DateField.SECOND, 3540);
//        psAccountUpdateWrapper.set("token_expiration_time", tokenExpirationTime);
//        psAccountUpdateWrapper.set("refresh_token", jsonObject.get("refresh_token").getAsString());
//        int refreshTokenExpiresIn = jsonObject.get("refresh_token_expires_in").getAsInt();
//        Date refreshTokenExpirationTime = DateUtil.offset(new Date(), DateField.SECOND, refreshTokenExpiresIn);
//        psAccountUpdateWrapper.set("refresh_token_expiration_time", refreshTokenExpirationTime);
//        baseMapper.update(null, psAccountUpdateWrapper);
//    }

    @Override
    public String refreshToken() {
        //先校验RefreshToken是否有效
        PsAccount psAccount = checkRefreshTokenAvailable();
        //刷新token
        String url = "https://ca.account.sony.com/api/authz/v3/oauth/token";
        Map<String, String> header = new HashMap<>();
        header.put("Cookie", "npsso=" + psAccount.getNpsso());
        header.put("Authorization", "Basic MDk1MTUxNTktNzIzNy00MzcwLTliNDAtMzgwNmU2N2MwODkxOnVjUGprYTV0bnRCMktxc1A=");
        header.put("User-Agent", "PlayStation/21090100 CFNetwork/1126 Darwin/19.5.0");
        header.put("Content-Type", "application/x-www-form-urlencoded");

        FormBody.Builder body = new FormBody.Builder();
        body.add("refresh_token", psAccount.getRefreshToken());
        body.add("scope", "psn:mobile.v2.core psn:clientapp");
        body.add("grant_type", "refresh_token");
        body.add("token_format", "jwt");

        String responseString = HttpUtils.okHttpPost(url, header, body);
        Gson gson = new Gson();
        JsonElement jsonElement = gson.fromJson(responseString, JsonElement.class);
        String accessToken;
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has("access_token")) {
                accessToken = jsonObject.get("access_token").getAsString();
                UpdateWrapper<PsAccount> psAccountUpdateWrapper = new UpdateWrapper<>();
                psAccountUpdateWrapper.set("token", accessToken);
                Date tokenExpirationTime = DateUtil.offset(new Date(), DateField.SECOND, 3540);
                psAccountUpdateWrapper.set("token_expiration_time", tokenExpirationTime);
                baseMapper.update(null, psAccountUpdateWrapper);
                return accessToken;
            } else {
                throw new P1cassoException(responseString);
            }
        } else {
            throw new RuntimeException("Json转换失败");
        }
    }

    @Override
    public PsAccount checkRefreshTokenAvailable() {
        PsAccount psAccount = baseMapper.selectList(null).get(0);
        Date now = new Date();

        if (StrUtil.isBlank(psAccount.getRefreshToken()) || StrUtil.isBlank(psAccount.getAccountid())) {
            return null;
        }
        int compare = DateUtil.compare(now, psAccount.getRefreshTokenExpirationTime());
        if (compare < 0) {
            return psAccount;
        } else {
            return null;
        }
    }

    @Override
    public void refreshTrophyStatistics() throws IOException {
        //获取access token
        String responseString = HttpUtils.okHttpGet(TROPHY_STATISTICS.getUrl(), login());
        Gson gson = new Gson();
        JsonElement jsonElement = gson.fromJson(responseString, JsonElement.class);
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has("trophyLevel")) {
                int trophyLevel = jsonObject.get("trophyLevel").getAsInt();
                JsonObject earnedTrophies = jsonObject.get("earnedTrophies").getAsJsonObject();
                int bronze = earnedTrophies.get("bronze").getAsInt();
                int silver = earnedTrophies.get("silver").getAsInt();
                int gold = earnedTrophies.get("gold").getAsInt();
                int platinum = earnedTrophies.get("platinum").getAsInt();
                PsAccount psAccount = new PsAccount(trophyLevel, bronze, silver, gold, platinum);
                baseMapper.update(psAccount, null);
            }
        }
    }

    @Override
    public void getTrophyTitles() {
        Map<String, String> header = login();
        header.put("Accept-Language", "zh-Hans");
        String responseString = HttpUtils.okHttpGet(TROPHY_TITLES.getUrl(), header);
        Gson gson = new Gson();
        JsonElement jsonElement = gson.fromJson(responseString, JsonElement.class);
        List<PsGameTrophy> updateOrInsertList = new ArrayList<>();

        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has("trophyTitles")) {
                JsonArray trophyTitles = jsonObject.get("trophyTitles").getAsJsonArray();
                TrophyTitles[] titles = gson.fromJson(trophyTitles, TrophyTitles[].class);
                updateOrInsertList.addAll(insertOrUpdateTrophyTitle(titles, header));
            } else {
                String errorMessage = jsonObject.get("error").getAsJsonObject().get("message").getAsString();
                throw new P1cassoException("无法获取TrophyTitles,错误：" + errorMessage);
            }
        }
        psGameTrophyService.saveOrUpdateBatch(updateOrInsertList);
    }

    @Override
    public List<PsGameTrophy> insertOrUpdateTrophyGroup(ConsoleEnum console, String npCommunicationId, Map<String, String> header) {
        String URL;
        if (console == PS5) {
            URL = TROPHY_GROUPS_DEFINED_PS5.getUrl(npCommunicationId);
        } else if (console == PS4_AND_OTHERS) {
            URL = TROPHY_GROUPS_DEFINED_PS4.getUrl(npCommunicationId);
        } else {
            throw new P1cassoException("未适配改游戏机");
        }
        Gson gson = new Gson();
        List<PsGameTrophy> updateOrInsertList = new ArrayList<>();
        String trophyGroupsResponseString = HttpUtils.okHttpGet(URL, header);

        JsonElement trophyGroupsJsonElement = gson.fromJson(trophyGroupsResponseString, JsonElement.class);
        if (trophyGroupsJsonElement.isJsonObject()) {
            TrophyGroup trophyGroup = gson.fromJson(trophyGroupsJsonElement, TrophyGroup.class);
            List<TrophyGroups> trophyGroups = trophyGroup.getTrophyGroups();
            for (TrophyGroups group : trophyGroups) {
                PsGameTrophy psGameTrophyGroup = new PsGameTrophy();
                psGameTrophyGroup.setPnpcommunicationid(npCommunicationId);
                psGameTrophyGroup.setTrophytitlename(trophyGroup.getTrophyTitleName());
                psGameTrophyGroup.setTrophytitleiconurl(trophyGroup.getTrophyTitleIconUrl());
                psGameTrophyGroup.setTrophytitleplatform(trophyGroup.getTrophyTitlePlatform());
                psGameTrophyGroup.setNpcommunicationid(npCommunicationId + "_" + group.getTrophyGroupId());
                psGameTrophyGroup.setTrophygroupid(group.getTrophyGroupId());
                psGameTrophyGroup.setTrophygroupname(group.getTrophyGroupName());
                psGameTrophyGroup.setTrophygroupiconurl(group.getTrophyGroupIconUrl());
                psGameTrophyGroup.setDefinedbronze(group.getDefinedTrophies().getBronze());
                psGameTrophyGroup.setDefinedsilver(group.getDefinedTrophies().getSilver());
                psGameTrophyGroup.setDefinedgold(group.getDefinedTrophies().getGold());
                psGameTrophyGroup.setDefinedplatinum(group.getDefinedTrophies().getPlatinum());
                updateOrInsertList.add(psGameTrophyGroup);
            }
        }
        return updateOrInsertList;
    }

    @Override
    public List<PsGameTrophy> insertOrUpdateTrophyTitle(TrophyTitles[] trophyTitles, Map<String, String> header) {
        List<PsGameTrophy> updateOrInsertList = new ArrayList<>();
        for (TrophyTitles title : trophyTitles) {
            PsGameTrophy psGameTrophy = new PsGameTrophy();
            psGameTrophy.setNpcommunicationid(title.getNpCommunicationId());
            psGameTrophy.setPnpcommunicationid("0");
            psGameTrophy.setTrophytitlename(title.getTrophyTitleName());
            psGameTrophy.setTrophytitleiconurl(title.getTrophyTitleIconUrl());
            psGameTrophy.setTrophytitleplatform(title.getTrophyTitlePlatform());
            psGameTrophy.setDefinedbronze(title.getDefinedTrophies().getBronze());
            psGameTrophy.setDefinedsilver(title.getDefinedTrophies().getSilver());
            psGameTrophy.setDefinedgold(title.getDefinedTrophies().getGold());
            psGameTrophy.setDefinedplatinum(title.getDefinedTrophies().getPlatinum());
            psGameTrophy.setEarnedbronze(title.getEarnedTrophies().getBronze());
            psGameTrophy.setEarnedsilver(title.getEarnedTrophies().getSilver());
            psGameTrophy.setEarnedgold(title.getEarnedTrophies().getGold());
            psGameTrophy.setEarnedplatinum(title.getEarnedTrophies().getPlatinum());
            psGameTrophy.setLastupdateddatetime(title.getLastUpdatedDateTime());
            if (title.isHasTrophyGroups()) {
                psGameTrophy.setHastrophygroups("1");
            } else {
                psGameTrophy.setHastrophygroups("0");
            }
            updateOrInsertList.add(psGameTrophy);
            //addGroup
            if (title.isHasTrophyGroups()) {
                String npCommunicationId = title.getNpCommunicationId();
                if (!Objects.equals(title.getTrophyTitlePlatform(), "PS5") && !title.getTrophyTitlePlatform().equals("PS5,PSPC")) {
                    updateOrInsertList.addAll(insertOrUpdateTrophyGroup(PS4_AND_OTHERS, npCommunicationId, header));
                } else if (title.getTrophyTitlePlatform().equals("PS5") || title.getTrophyTitlePlatform().equals("PS5,PSPC")) {
                    updateOrInsertList.addAll(insertOrUpdateTrophyGroup(PS5, npCommunicationId, header));
                }
            }
        }
        return updateOrInsertList;
    }

    @Override
    public PsAccount getTrophyStatistics() {
        return baseMapper.selectOne(null);
    }

    @Override
    public void updateGroupsTrophy() {
        Map<String, String> header = this.login();
        QueryWrapper<PsGameTrophy> psGameTrophyQueryWrapper = new QueryWrapper<>();
        psGameTrophyQueryWrapper.eq("hasTrophyGroups", "1");
        psGameTrophyQueryWrapper.eq("pnpCommunicationId", "0");
        List<PsGameTrophy> hasTrophyGroupsList = psGameTrophyService.list(psGameTrophyQueryWrapper);
        List<PsGameTrophy> updateList = new ArrayList<>();
        for (PsGameTrophy psGameTrophy : hasTrophyGroupsList) {
            String console = psGameTrophy.getTrophytitleplatform();
            String npcommunicationid = psGameTrophy.getNpcommunicationid();
            String URL = null;
            if (Objects.equals(console, "PS5")) {
                URL = TROPHY_GROUPS_EARNED_PS5.getUrl(npcommunicationid);
            } else if (Objects.equals(console, "PS4")) {
                URL = TROPHY_GROUPS_EARNED_PS4.getUrl(npcommunicationid);
            } else {
                log.warn("未适配该游戏机");
                continue;
            }
            Gson gson = new Gson();
            String trophyGroupsResponseString = HttpUtils.okHttpGet(URL, header);
            JsonElement trophyGroupsJsonElement = gson.fromJson(trophyGroupsResponseString, JsonElement.class);
            if (trophyGroupsJsonElement.isJsonObject()) {
                TrophyTitles trophyTitles = gson.fromJson(trophyGroupsJsonElement, TrophyTitles.class);
                List<TrophyGroups> trophyGroupsList = trophyTitles.getTrophyGroups();
                for (TrophyGroups trophyGroups : trophyGroupsList) {
                    String trophyGroupId = trophyGroups.getTrophyGroupId();
                    Trophies earnedTrophies = trophyGroups.getEarnedTrophies();
                    PsGameTrophy gameTrophy = new PsGameTrophy();
                    gameTrophy.setNpcommunicationid(npcommunicationid + "_" + trophyGroupId);
                    gameTrophy.setEarnedbronze(earnedTrophies.getBronze());
                    gameTrophy.setEarnedsilver(earnedTrophies.getSilver());
                    gameTrophy.setEarnedgold(earnedTrophies.getGold());
                    gameTrophy.setEarnedplatinum(earnedTrophies.getPlatinum());
                    updateList.add(gameTrophy);
                }
            }
        }
        psGameTrophyService.updateBatchById(updateList);
    }


    private static String extractCodeValue(String url) {
        Pattern pattern = Pattern.compile("code=([^&]+)");
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
}