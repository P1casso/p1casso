package com.p1casso.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.p1casso.Utils.Result;
import com.p1casso.entity.GamePlan;
import com.p1casso.mapper.GamePlanMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {

    @Resource
    private GamePlanMapper gamePlanMapper;

    @GetMapping("/plan_list")
    public Result<List<GamePlan>> getAllGamePlan() {
        QueryWrapper<GamePlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("play_start_date");
        // List<GamePlan> gamePlanList = gamePlanMapper.selectList(queryWrapper);
        List<GamePlan> gamePlanList = gamePlanMapper.getListOrderByPlayStartDate();
        return Result.success(gamePlanList);
    }

    @PostMapping("/add/plan")
    public Result<String> addGamePlan(@RequestBody GamePlan gamePlan) {
        gamePlanMapper.insert(gamePlan);
        return Result.success();
    }

    @PostMapping("/edit/plan")
    public Result<String> editGamePlan(@RequestBody GamePlan gamePlan) {
        int i = gamePlanMapper.updateById(gamePlan);
        List<GamePlan> gamePlanList = gamePlanMapper.selectList(null);
        System.out.printf(String.valueOf(i));
        GamePlan gamePlan1 = gamePlanMapper.selectById("1702266041231065089");
        return Result.success();
    }

    @PostMapping("/plan/delete/{id}")
    public Result<String> deleteGamePlan(@PathVariable(value = "id") String id) {
        gamePlanMapper.deleteById(id);
        return Result.success();
    }



}
