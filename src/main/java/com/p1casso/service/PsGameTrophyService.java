package com.p1casso.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.p1casso.entity.PsGameTrophy;

/**
 * @author p1cas
 * @description 针对表【ps_game_trophy】的数据库操作Service
 * @createDate 2024-01-08 09:24:26
 */
public interface PsGameTrophyService extends IService<PsGameTrophy> {

    /**
     * 根据NpCommunicationId获取游戏奖杯获得情况
     *
     * @param id CommunicationId
     * @return 奖杯获得情况
     */
    PsGameTrophy getTrophyGroupByNpCommunicationId(String id);
}
