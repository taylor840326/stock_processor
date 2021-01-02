package com.imsql.stock_processor.api;

import com.imsql.stock_processor.service.kafkaconsumer.StockConsumerForDaily;
import com.imsql.stock_processor.service.processor.StockDailyProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Stock {
    @Autowired
    StockConsumerForDaily stockConsumerForDaily;

    @Autowired
    StockDailyProcessor stockDailyProcessor;

    @GetMapping( "/stock/daily/enable")
    public void runStockConsumerForDaily(){
        try {
            stockConsumerForDaily.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/stock/daily/get")
    public void getStockDailyData(){
        try {
            stockDailyProcessor.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
