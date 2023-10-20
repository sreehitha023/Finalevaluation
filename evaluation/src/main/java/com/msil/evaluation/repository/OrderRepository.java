package com.msil.evaluation.repository;
import com.msil.evaluation.constants.QueryConstants;
import com.msil.evaluation.entity.Order;
import com.msil.evaluation.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query(QueryConstants.Find_By_Orders)
    List<Order> findBuyOrders();

    @Query(QueryConstants.Find_By_UserOrderId_OrderType)
    List<Order> findSellOrders();
    List<Order> findByUserOrdersIdAndOrderTypeAndQuantityGreaterThanAndStatus(UserDetail userDetail, String orderType, int quantity, String status);

    List<Order> findByUserOrdersId( UserDetail currentUserDetails);
}


