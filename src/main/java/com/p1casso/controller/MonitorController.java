package com.p1casso.controller;

import com.p1casso.Utils.Result;
import com.p1casso.entity.MonitorOrnaments;
import com.p1casso.entity.PriceTrend;
import com.p1casso.mapper.MonitorOrnamentsMapper;
import com.p1casso.service.PriceTrendService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 监视饰品Controller
 */
@RestController
public class MonitorController {
    @Resource
    private PriceTrendService priceTrendService;
    @Resource
    private MonitorOrnamentsMapper monitorOrnamentsMapper;

    /**
     * 获取需要监视的皮肤价格
     *
     * @return 需要监视的皮肤价格
     */
    @GetMapping("/monitor")
    //@Scheduled(cron = "* * * * * *")
    public Result<List<PriceTrend>> getPriceNeedMonitor() {
        List<PriceTrend> priceTrends = priceTrendService.TimeRecordedPrices();
        return Result.success(priceTrends);
    }

    /**
     * 添加需要监视的饰品
     */
    @PostMapping("/monitor/{ornaments_id}")
    public Result addNeedMonitor(@PathVariable(value = "ornaments_id") String ornamentsId) {
        int i = monitorOrnamentsMapper.insert(new MonitorOrnaments(ornamentsId));
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    @DeleteMapping("/monitor/{id}")
    public Result deleteNeedMonitor(@PathVariable(value = "id") String id) {
        int i = monitorOrnamentsMapper.deleteById(id);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    @GetMapping("/monitor/list")
    public Result< List<MonitorOrnaments>> getNeedMonitorList() {
        List<MonitorOrnaments> monitorOrnamentsList = monitorOrnamentsMapper.selectList(null);
        System.out.println(monitorOrnamentsList);
        return Result.success(monitorOrnamentsList);
    }
}
