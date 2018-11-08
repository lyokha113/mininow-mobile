package com.longnh.mobile.mininow.ultils;

import com.longnh.mobile.mininow.entity.Customer;

public class ConstantManager {

    public static final String customerID = "CU-1";
    public static final String shipperID = "SP-1";
    public static final String ORDER_TEMPORARY = "order_temporary";
    public static final String STORE_ADDRESS = "store_address";
    public static final String STORE_ID = "store_id";
    public static final String STORE_NAME = "store_name";
    public static final int ORDER_WAITING = 1;
    public static final int ORDER_APPROVE = 2;
    public static final int ORDER_ACCEPTED = 3;
    public static final int ORDER_PICKED = 4;
    public static final int ORDER_DONE = 5;
    public static final int ORDER_REJECTED = 6;
    public static final int ORDER_FAILED = 7;
    public static final int SHIP_COST = 3000;
    public static Customer customer;
}
