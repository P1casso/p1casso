package com.p1casso.mapper;

import com.p1casso.entity.PriceTrend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author p1casso
 * @description 针对表【price_trend(价格走势记录)】的数据库操作Mapper
 * @createDate 2023-03-27 14:35:13
 * @Entity com.p1casso.buffmonitor.entity.PriceTrend
 */
@Mapper
public interface PriceTrendMapper extends BaseMapper<PriceTrend> {

}




