package com.p1casso.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.p1casso.entity.PsGameTrophy;
import com.p1casso.mapper.PsGameTrophyMapper;
import com.p1casso.service.PsGameTrophyService;
import org.springframework.stereotype.Service;

/**
 * @author p1cas
 * @description 针对表【ps_game_trophy】的数据库操作Service实现
 * @createDate 2024-01-08 09:24:26
 */
@Service
public class PsGameTrophyServiceImpl extends ServiceImpl<PsGameTrophyMapper, PsGameTrophy> implements PsGameTrophyService {

    @Override
    public PsGameTrophy getTrophyGroupByNpCommunicationId(String id) {
        QueryWrapper<PsGameTrophy> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("npCommunicationId", id);
        return baseMapper.selectOne(queryWrapper);
    }
}




