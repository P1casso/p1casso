package com.p1casso.service;

import com.p1casso.entity.MonitorOrnaments;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author p1casso
* @description 针对表【monitor_ornaments(待监听的饰品)】的数据库操作Service
* @createDate 2023-03-27 14:25:45
*/
public interface MonitorOrnamentsService extends IService<MonitorOrnaments> {

    List<MonitorOrnaments> getNeedMonitorOrnaments();

}
