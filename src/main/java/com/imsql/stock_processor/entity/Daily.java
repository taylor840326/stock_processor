package com.imsql.stock_processor.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "t_daily")
public class Daily implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ts_code;
    private String trade_date;
    @Column(name = "open_price")
    private Float open;
    @Column(name = "high_price")
    private Float high;
    @Column(name = "low_price")
    private Float low;
    @Column(name = "close_price")
    private Float close;
    private Float pre_close;
    @Column(name = "change_price")
    private Float change;
    private Float pct_chg;
    private Float vol;
    private Float amount;
}
