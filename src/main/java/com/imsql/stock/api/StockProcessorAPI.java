package com.imsql.stock.api;

import com.imsql.stock.service.SparkStreaming;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockProcessorAPI{

    @Autowired
    SparkStreaming sparkStreaming;

    @GetMapping("/api/stock/daily")
    public void executeDailyStock() throws Exception {
        sparkStreaming.run();
    }
    
}
