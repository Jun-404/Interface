package com.example.publisher.service.impl;

import com.example.publisher.service.ESService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.support.ValueType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//将当前对象的创建交给Spring容器进行管理
@Service
public class ESServiceImpl implements ESService {

    //将ES的客户端操作对象注入到Service中
    @Autowired
    JestClient jestClient;
    @Override
    public Long getDauTotal(String date) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(new MatchAllQueryBuilder());
        String query = sourceBuilder.toString();
        String indexName = "gmall1015_dau_info_"+date+"-query";
        Search search = new Search.Builder(query)
                .addIndex(indexName)
                .build();
        Long total = 0L;
        try {
            SearchResult result = jestClient.execute(search);
            total = result.getTotal();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("查询ES失败");
        }
        return total;
    }

    /*
    * GET gmall1015_dau_info_2021-10-18-query/_search
{
  "aggs": {
    "groupBy_hr": {
      "terms": {
        "field": "hr",
        "size": 24
      }
    }
  }
}*/

    @Override
    public Map<String, Long> getDauHour(String date) {
        String indexName = "gmall1015_dau_info_"+date+"-query";
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("groupBy_hr").field("hr").size(24);
        //TermsAggregationBuilder termsAggregationBuilder = new TermsAggregationBuilder("groupBy_hr", ValueType.LONG).field("hr").size(24);
        searchSourceBuilder.aggregation(termsAggregationBuilder);
        String query = searchSourceBuilder.toString();
        Search search = new Search.Builder(query)
                .addIndex(indexName)
                .build();
        try {
            SearchResult result = jestClient.execute(search);
            HashMap<String, Long> hourMap = new HashMap<>();
            TermsAggregation termsAgg = result.getAggregations().getTermsAggregation("groupBy_hr");
            if (termsAgg != null){
                List<TermsAggregation.Entry> buckets = termsAgg.getBuckets();
                for (TermsAggregation.Entry bucket : buckets) {
                    hourMap.put(bucket.getKey(),bucket.getCount());
                }
            }
            return hourMap;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ES查询异常");
        }
    }
}
