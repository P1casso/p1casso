package com.p1casso.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.p1casso.entity.PsAccount;
import com.p1casso.entity.PsGameTrophy;
import com.p1casso.enums.ConsoleEnum;
import com.p1casso.vo.TrophyTitles;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author p1casso
 * @description 针对表【ps_account】的数据库操作Service
 * @createDate 2023-12-31 12:33:41
 */
public interface PsAccountService extends IService<PsAccount> {

    /**
     * 刷新token
     *
     * @return 刷新后的accessToken
     */
    String refreshToken() throws IOException;

    /**
     * 校验RefreshToken是否可用
     *
     * @return 如果可用返回账户信息，否则返回null
     */
    PsAccount checkRefreshTokenAvailable();

    /**
     * 登录
     *
     * @return 带认证的HeadMap
     */
    Map<String, String> login() throws IOException;

    /**
     * 刷新奖杯统计数据（白金：xxx,金：xxx,银：xxx,铜：xxx）
     */
    void refreshTrophyStatistics() throws IOException;

    /**
     * 获取账号所有游戏奖杯统计
     * （游戏奖杯：白金：xxx,金：xxx,银：xxx,铜：xxx；已获得：白金：xxx,金：xxx,银：xxx,铜：xxx）
     */
    void getTrophyTitles() throws IOException;

    /**
     * 更新/新增游戏的DLC奖杯信息
     * （游戏A，本体奖杯：白金：xxx,金：xxx,银：xxx,铜：xxx；DLC1奖杯：白金：xxx,金：xxx,银：xxx,铜：xxx）
     *
     * @param console           主机类型
     * @param npCommunicationId 游戏npCommunicationId
     * @param header            认证信息
     * @return 需要插入或更新的的数据
     */
    List<PsGameTrophy> insertOrUpdateTrophyGroup(ConsoleEnum console, String npCommunicationId, Map<String, String> header);

    /**
     * 更新/新增游戏奖杯信息（包括DLC）
     *
     * @param trophyTitles 接口返回数据的trophyTitles
     * @param header       认证信息
     * @return 需要插入或更新的的数据
     */
    List<PsGameTrophy> insertOrUpdateTrophyTitle(TrophyTitles[] trophyTitles, Map<String, String> header);

    PsAccount getTrophyStatistics();

    /**
     * 更新游戏分组的奖杯获取统计
     */
    void updateGroupsTrophy() throws IOException;

}
