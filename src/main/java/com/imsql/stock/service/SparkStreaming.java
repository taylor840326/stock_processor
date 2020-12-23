package com.imsql.stock.service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.imsql.stock.entity.Daily;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.stereotype.Service;

@Service
public class SparkStreaming implements Serializable {

    public void run() throws Exception {

        // TODO Auto-generated method stub
        SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("sparkstreaming");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));

        HashMap<String, Object> kafkaParams = new HashMap<>();

        kafkaParams.put("bootstrap.servers", "localhost:9092");
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", "spring_spark_streaming_group01");
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);

        List<String> topics = Arrays.asList("stock");

        JavaInputDStream<org.apache.kafka.clients.consumer.ConsumerRecord<String, String>> stream = KafkaUtils
                .createDirectStream(jssc, LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams));

        stream.transform(new Function<JavaRDD<ConsumerRecord<String, String>>, JavaRDD<Daily>>() {

            @Override
            public JavaRDD<Daily> call(JavaRDD<ConsumerRecord<String, String>> v1) throws Exception {
                // TODO Auto-generated method stub
                JavaRDD<Daily> m1 = v1.map(new Function<ConsumerRecord<String, String>, Daily>() {

                    @Override
                    public Daily call(ConsumerRecord<String, String> v1) throws Exception {
                        // TODO Auto-generated method stub
                        Gson gson = new Gson();
                        Daily d = gson.fromJson(v1.value(), Daily.class);
                        return d;
                    }

                });
                return m1;
            }

        }).foreachRDD(new VoidFunction<JavaRDD<Daily>>() {

            @Override
            public void call(JavaRDD<Daily> t) throws Exception {
                // TODO Auto-generated method stub
                t.foreachPartition(new VoidFunction<Iterator<Daily>>() {

                    @Override
                    public void call(Iterator<Daily> t) throws Exception {
                        // TODO Auto-generated method stub
                        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/stock",
                                "stock", "stock");
                        connection.setAutoCommit(true);
                        String sql = "insert into t_daily(amount,change_price,close_price,high_price,low_price,open_price,pct_chg,pre_close,trade_date,ts_code,vol) values(?,?,?,?,?,?,?,?,?,?,?)";
                        PreparedStatement pstmt = connection.prepareStatement(sql);
                        t.forEachRemaining(d -> {
                            try {
                                pstmt.setFloat(1, d.getAmount());
                                pstmt.setFloat(2, d.getChange());
                                pstmt.setFloat(3, d.getClose());
                                pstmt.setFloat(4, d.getHigh());
                                pstmt.setFloat(5, d.getLow());
                                pstmt.setFloat(6, d.getOpen());
                                pstmt.setFloat(7, d.getPct_chg());
                                pstmt.setFloat(8, d.getPre_close());
                                pstmt.setString(9, d.getTrade_date());
                                pstmt.setString(10, d.getTs_code());
                                pstmt.setFloat(11, d.getVol());
                                pstmt.addBatch();

                            } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        });
                        pstmt.executeBatch();

                    };

                });
            }

        });
        jssc.start();
        jssc.awaitTermination();

    }

}
