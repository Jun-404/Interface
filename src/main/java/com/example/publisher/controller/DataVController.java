package com.example.publisher.controller;

import com.example.publisher.service.MySQLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class DataVController {
    @Autowired
    MySQLService mySQLService;

    @RequestMapping("/trademark-sum")
    public Object trademarkSum(@RequestParam("start_date") String startDate,
                           @RequestParam("end_date") String endDate,
                           @RequestParam("topN") int topN){
        List<Map> trademardSum = mySQLService.getTrademardStat(startDate,
                endDate,topN);
        return trademardSum ;

    }
}
