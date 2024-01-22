package com.p1casso.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.p1casso.Utils.HttpUtils;
import com.p1casso.entity.MonitorOrnaments;
import com.p1casso.entity.Ornaments;
import com.p1casso.entity.PriceTrend;
import com.p1casso.mapper.PriceTrendMapper;
import com.p1casso.service.MonitorOrnamentsService;
import com.p1casso.service.PriceTrendService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author p1casso
 * @description 针对表【price_trend(价格走势记录)】的数据库操作Service实现
 * @createDate 2023-03-27 14:35:13
 */
@Service
public class PriceTrendServiceImpl extends ServiceImpl<PriceTrendMapper, PriceTrend> implements PriceTrendService {
    private static final String Url = "https://buff.163.com/api/market/goods/sell_order?game=csgo&goods_id=";

    @Resource
    private MonitorOrnamentsService monitorOrnamentsService;

    @Override
    public List<PriceTrend> TimeRecordedPrices() {
        for (MonitorOrnaments monitorOrnaments : monitorOrnamentsService.getNeedMonitorOrnaments()) {
            List<PriceTrend> list = new ArrayList<>();
            JSONObject response = JSONObject.parseObject(HttpUtils.Get(Url + monitorOrnaments.getOrnamentsId()));
            if (Objects.equals(response.getString("code"), "OK")) {
                String name = response.getJSONObject("data").getJSONObject("goods_infos").getJSONObject(monitorOrnaments.getOrnamentsId()).getString("name");
                JSONArray jsonArray = response.getJSONObject("data").getJSONArray("items");
                String jsonString = JSONObject.toJSONString(jsonArray);
                List<Ornaments> ornaments = JSONArray.parseArray(jsonString, Ornaments.class);
                List<Double> priceList = new ArrayList<>();
                for (Ornaments ornament : ornaments) {

                    priceList.add(new Double(ornament.getPrice()));
                }
                Double min = Collections.min(priceList);
                PriceTrend priceTrend = new PriceTrend(monitorOrnaments.getOrnamentsId(), name, new BigDecimal(min.toString()));
                list.add(priceTrend);
                int i = baseMapper.insert(priceTrend);
                if (i != 1) {
                    throw new RuntimeException("插入数据库失败");
                }
                return list;
            } else {
                throw new RuntimeException("读取接口失败");
            }
        }
        return null;
    }
}




