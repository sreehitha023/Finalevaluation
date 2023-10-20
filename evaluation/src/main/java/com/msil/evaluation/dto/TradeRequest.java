package com.msil.evaluation.dto;

import com.msil.evaluation.entity.Order;
import com.msil.evaluation.entity.UserDetail;
import lombok.Data;

@Data
public class TradeRequest {
    String groupName;
    String symbolName;
    Long groupId;
    AddSymbol symbol;
    Order order;
    String orderOperation;
    String orderId;
}
