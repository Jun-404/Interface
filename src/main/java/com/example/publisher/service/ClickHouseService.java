package com.example.publisher.service;

import java.math.BigDecimal;
import java.util.Map;

public interface ClickHouseService {

    //获取指定日期总的交易额
    BigDecimal getOrderAmountTotal(String date);

    //获取指定日期的分时交易额
    Map<String,BigDecimal> getOrderAmountHour(String date);
}
