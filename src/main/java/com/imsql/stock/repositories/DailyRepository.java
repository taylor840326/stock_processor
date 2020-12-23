package com.imsql.stock.repositories;

import com.imsql.stock.entity.Daily;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRepository extends JpaRepository<Daily,Long>{
    
}
