package com.example.publisher.service.impl;

import com.example.publisher.mapper.OrderWideMapper;
import com.example.publisher.service.ClickHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClickHouseServiceImpl implements ClickHouseService {

    @Autowired
    OrderWideMapper orderWideMapper;
    @Override
    public BigDecimal getOrderAmountTotal(String date) {
        return orderWideMapper.selectOrderAmountTotal(date);
    }

    @Override
    public Map<String, BigDecimal> getOrderAmountHour(String date) {
        Map<String,BigDecimal> rsMap = new HashMap<String,BigDecimal>();
        List<Map> mapList = orderWideMapper.selectOrderAmountHour(date);
        for (Map map : mapList) {
            rsMap.put(String.format("%02d",map.get("hr")),(BigDecimal) map.get("am"));
        }
        return rsMap;
    }
}
