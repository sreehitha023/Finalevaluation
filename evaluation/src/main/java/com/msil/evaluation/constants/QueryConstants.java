package com.msil.evaluation.constants;

public class QueryConstants {
    public static final String Find_By_Orders = "SELECT o FROM Order o WHERE o.orderType = 'BUY'";
    public static final String Find_By_UserOrderId_OrderType = "SELECT o FROM Order o WHERE o.orderType = 'SELL'";
}
