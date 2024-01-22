package com.p1casso.service;

import com.p1casso.entity.PriceTrend;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author p1casso
 * @description 针对表【price_trend(价格走势记录)】的数据库操作Service
 * @createDate 2023-03-27 14:35:13
 */
public interface PriceTrendService extends IService<PriceTrend> {
    List<PriceTrend> TimeRecordedPrices();
}
