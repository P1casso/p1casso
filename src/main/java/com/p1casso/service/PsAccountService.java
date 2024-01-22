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
     * @return
     */
    String refreshToken() throws IOException;

    PsAccount checkRefreshTokenAvailable();

    Map<String, String> login() throws IOException;

    /**
     * 获取奖杯统计数据并写入本地
     */
    void refreshTrophyStatistics() throws IOException;

    void getTrophyTitles() throws IOException;

    /**
     * 更新/新增游戏的DLC奖杯信息
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
    void updateGroupsTrophy(String npCommunicatioinId, ConsoleEnum console) throws IOException;

}
