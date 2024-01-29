package com.p1casso.service;

import com.p1casso.entity.PsTrophy;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.util.List;

/**
 * @author p1cas
 * @description 针对表【ps_trophy】的数据库操作Service
 * @createDate 2024-01-20 15:35:44
 */
public interface PsTrophyService extends IService<PsTrophy> {

    /**
     * 获取奖杯列表（不包含是否获得等信息）
     */
    void InsertOrUpdateTrophy() throws IOException;

    List<PsTrophy> getTrophyListByNpCommunicationId(String NpCommunicationId);



    void InsertOrUpdateTrophyEarnedState() throws IOException;
}
