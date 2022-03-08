package com.example.publisher.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class OrderWideMapperImpl implements OrderWideMapper{

    //抽取DBUtil工具类
    @Override
    public BigDecimal selectOrderAmountTotal(String date) {
        //执行SQL
        //处理结果集
        return null;
    }

    @Override
    public List<Map> selectOrderAmountHour(String date) {
        //执行SQL
        //处理结果集
        return null;
    }
}
