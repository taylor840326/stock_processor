package com.imsql.stock_processor.service.processor;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import py4j.commands.Command;

@Service
public class StockDailyProcessor {
    @Scheduled(fixedRate = 15000)
    public void run() throws Exception {
        SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("StockDailyProcessor");

        SparkSession ss = SparkSession.builder().config(conf).getOrCreate();
        Dataset<Row> tDaily = ss.read().format("jdbc")
                .option("url", "jdbc:mysql://127.0.0.1:3306/stock")
                .option("user", "stock")
                .option("password", "stock")
                .option("query", "select * from t_daily")
                .load();

        tDaily.show();
        tDaily.printSchema();

    }
}
