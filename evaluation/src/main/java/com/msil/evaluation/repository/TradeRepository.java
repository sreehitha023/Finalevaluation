package com.msil.evaluation.repository;

import com.msil.evaluation.entity.Trade;
import com.msil.evaluation.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade,Long> {

    List<Trade> findByUserTradeId(UserDetail currentUserDetail);

}
