package com.imsql.stock.service;

import java.util.Iterator;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;


@Service
public class SparkStreaming {

    public void run() throws Exception {
        // TODO Auto-generated method stub
        SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("sparkstreaming");
        SparkSession ss = SparkSession.builder().config(conf).getOrCreate();

        Dataset<String> textFile = ss.read().textFile("/etc/hosts");

        textFile.show();
    }
    
}
