package com.p1casso.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.p1casso.entity.MonitorOrnaments;
import com.p1casso.service.MonitorOrnamentsService;
import com.p1casso.mapper.MonitorOrnamentsMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author p1casso
 * @description 针对表【monitor_ornaments(待监听的饰品)】的数据库操作Service实现
 * @createDate 2023-03-27 14:25:45
 */
@Service
public class MonitorOrnamentsServiceImpl extends ServiceImpl<MonitorOrnamentsMapper, MonitorOrnaments> implements MonitorOrnamentsService {

    @Override
    public List<MonitorOrnaments> getNeedMonitorOrnaments() {
        List<MonitorOrnaments> monitorOrnamentList = baseMapper.selectList(null);
        return monitorOrnamentList;
    }
}




