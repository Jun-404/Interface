package com.example.publisher.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TrademarkStatMapper {
    List<Map> selectTradeSum(@Param("start_date") String startDate,
                             @Param("end_date") String endDate,
                             @Param("topN") int topN);
}
