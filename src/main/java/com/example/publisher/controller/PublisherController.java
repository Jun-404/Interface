package com.example.publisher.controller;

import com.example.publisher.service.ClickHouseService;
import com.example.publisher.service.ESService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

//发布数据接口
@RestController
public class PublisherController {

    @Autowired
    ESService esService;

    @Autowired
    ClickHouseService clickHouseService;

    @RequestMapping("realtime-total")
    public Object realtimeTotal(@RequestParam("date") String dt){
        List<Map<String,Object>> rsList = new ArrayList<Map<String,Object>>();
        Map<String,Object> dauMap = new HashMap<>();
        dauMap.put("id","dau");
        dauMap.put("name","新增设备");
        Long dauTotal = esService.getDauTotal(dt);
        if(dauTotal != null){
            dauMap.put("value",dauTotal);
        } else {
            dauMap.put("value",0L);
        }

        rsList.add(dauMap);

//        Map<String,Object> newMidMap = new HashMap();
//        newMidMap.put("id","new_mid");
//        newMidMap.put("name","新增设备");
//        newMidMap.put("value",233);
//        rsList.add(newMidMap);

        //新增交易额
        Map<String,Object> orderAmountMap = new HashMap();
        orderAmountMap.put("id","order_amount");
        orderAmountMap.put("name","新增交易额");
        orderAmountMap.put("value", clickHouseService.getOrderAmountTotal(dt));
        rsList.add(orderAmountMap);

        return rsList;
    }
    //分时统计
    @RequestMapping("realtime-hour")
    public Object realtimeHour(@RequestParam("id") String id,@RequestParam("date") String dt){
        if (id.equals("dau")) {
            //获取今天的日活统计
            Map<String, Map<String, Long>> rsMap = new HashMap<>();
            Map<String, Long> tdMap = esService.getDauHour(dt);
            rsMap.put("today", tdMap);
            //获取昨天的日活统计
            //根据当前日期获取昨天日期的字符串
            String yd = getYd(dt);
            Map<String, Long> ydMap = esService.getDauHour(yd);
            rsMap.put("yesterday", ydMap);

            return rsMap;
        }else if ("order_amount".equals(id)){
            //获取今天的交易额统计
            Map<String, Map<String, BigDecimal>> amountMap = new HashMap<>();
            Map<String, BigDecimal> tdMap = clickHouseService.getOrderAmountHour(dt);
            amountMap.put("today", tdMap);
            //获取昨天的交易额统计
            String yd = getYd(dt);
            Map<String, BigDecimal> ydMap = clickHouseService.getOrderAmountHour(yd);
            amountMap.put("yesterday", ydMap);
            return amountMap;
        }else {
            return null;
        }

    }

    private String getYd(String td) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String yd = null;
        try {
            Date tdDate = dateFormat.parse(td);
            Date ydDate = DateUtils.addDays(tdDate, -1);
            yd = dateFormat.format(ydDate);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("日期格式转变失败");
        }
        return yd;

    }
}
