package com.p1casso.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.p1casso.Utils.HttpUtils;
import com.p1casso.entity.PsGameTrophy;
import com.p1casso.entity.PsTrophy;
import com.p1casso.service.PsTrophyService;
import com.p1casso.mapper.PsTrophyMapper;
import com.p1casso.vo.Trophies;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.p1casso.enums.PSUrlEnum.*;

/**
 * @author p1cas
 * @description 针对表【ps_trophy】的数据库操作Service实现
 * @createDate 2024-01-20 15:35:44
 */
@Service
public class PsTrophyServiceImpl extends ServiceImpl<PsTrophyMapper, PsTrophy> implements PsTrophyService {

    @Resource
    private PsAccountServiceImpl psAccountService;

    @Resource
    private PsGameTrophyServiceImpl psGameTrophyService;

    @Override
    public void InsertOrUpdateTrophy() {
        Map<String, String> header = psAccountService.login();
        QueryWrapper<PsGameTrophy> psGameTrophyQueryWrapper = new QueryWrapper<>();
        psGameTrophyQueryWrapper.eq("pnpCommunicationId", "0");
        psGameTrophyQueryWrapper.ne("npcommunicationid", "0");
        List<PsGameTrophy> gameList = psGameTrophyService.list(psGameTrophyQueryWrapper);
        List<PsTrophy> updateList = new ArrayList<>();
        for (PsGameTrophy gameTrophy : gameList) {
            String console = gameTrophy.getTrophytitleplatform();
            String npcommunicationid = gameTrophy.getNpcommunicationid();
            String URL;
            if (Objects.equals(console, "PS5")) {
                URL = TROPHY_LIST_PS5.getUrl(npcommunicationid);
            } else if (Objects.equals(console, "PS4")) {
                URL = TROPHY_LIST_PS4.getUrl(npcommunicationid);
            } else {
                log.warn("未适配该游戏机");
                continue;
            }
            Gson gson = new Gson();
            String responseString = HttpUtils.okHttpGet(URL, header);
            JsonElement jsonElement = gson.fromJson(responseString, JsonElement.class);
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (jsonObject.has("trophies")) {
                    JsonArray jsonArray = jsonObject.get("trophies").getAsJsonArray();
                    List<Trophies> trophiesList = gson.fromJson(jsonArray, new TypeToken<List<Trophies>>() {
                    }.getType());
                    for (Trophies trophies : trophiesList) {
                        PsTrophy psTrophy = new PsTrophy();
                        psTrophy.setId(npcommunicationid + "_" + trophies.getTrophyId());
                        psTrophy.setNpcommunicationid(npcommunicationid);
                        psTrophy.setTrophyGroupId(trophies.getTrophyGroupId());
                        psTrophy.setTrophyid(trophies.getTrophyId());
                        psTrophy.setTrophyname(trophies.getTrophyName());
                        psTrophy.setTrophydetail(trophies.getTrophyDetail());
                        psTrophy.setTrophyiconurl(trophies.getTrophyIconUrl());
                        updateList.add(psTrophy);
                    }
                }
            }
        }
        super.saveOrUpdateBatch(updateList);
    }

    @Override
    public void InsertOrUpdateTrophyEarnedState() {
        Map<String, String> header = psAccountService.login();
        QueryWrapper<PsGameTrophy> psGameTrophyQueryWrapper = new QueryWrapper<>();
        psGameTrophyQueryWrapper.ne("npCommunicationId", "0");
        psGameTrophyQueryWrapper.eq("pnpCommunicationId", "0");
        List<PsGameTrophy> gameList = psGameTrophyService.list(psGameTrophyQueryWrapper);
        List<PsTrophy> updateList = new ArrayList<>();
        for (PsGameTrophy gameTrophy : gameList) {
            String console = gameTrophy.getTrophytitleplatform();
            String npcommunicationid = gameTrophy.getNpcommunicationid();
            String URL;
            if (Objects.equals(console, "PS5")) {
                URL = TROPHY_LIST_EARNED_PS5.getUrl(npcommunicationid);
            } else if (Objects.equals(console, "PS4")) {
                URL = TROPHY_LIST_EARNED_PS4.getUrl(npcommunicationid);
            } else {
                log.warn("未适配该游戏机");
                continue;
            }
            Gson gson = new Gson();
            String responseString = HttpUtils.okHttpGet(URL, header);
            JsonElement jsonElement = gson.fromJson(responseString, JsonElement.class);
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (jsonObject.has("trophies")) {
                    JsonArray jsonArray = jsonObject.get("trophies").getAsJsonArray();
                    List<Trophies> trophiesList = gson.fromJson(jsonArray, new TypeToken<List<Trophies>>() {
                    }.getType());
                    for (Trophies trophies : trophiesList) {
                        PsTrophy psTrophy = new PsTrophy();
                        psTrophy.setId(npcommunicationid + "_" + trophies.getTrophyId());
                        if (trophies.getEarned()) {
                            psTrophy.setEarned(0);
                        } else {
                            psTrophy.setEarned(1);
                        }
                        if (trophies.getEarnedDateTime() != null) {
                            psTrophy.setEarneddatetime(trophies.getEarnedDateTime());
                        }
                        updateList.add(psTrophy);
                    }
                }
            }
        }
        super.saveOrUpdateBatch(updateList);
    }

    @Override
    public List<PsTrophy> getTrophyListByNpCommunicationId(String npCommunicationId) {
        QueryWrapper<PsTrophy> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("npCommunicationId", npCommunicationId);
        queryWrapper.orderByAsc("trophyId");
        return baseMapper.selectList(queryWrapper);
    }
}




