package com.example.publisher.service;

import java.util.List;
import java.util.Map;

public interface MySQLService {
    List<Map> getTrademardStat(String startDate, String endDate,int topN);
}
