package com.p1casso.mapper;

import com.p1casso.entity.GamePlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author p1cas
* @description 针对表【game_plan】的数据库操作Mapper
* @createDate 2023-09-14 14:46:50
* @Entity com.p1casso.buffmonitor.entity.GamePlan
*/
@Mapper
public interface GamePlanMapper extends BaseMapper<GamePlan> {

    List<GamePlan> getListOrderByPlayStartDate();
}




