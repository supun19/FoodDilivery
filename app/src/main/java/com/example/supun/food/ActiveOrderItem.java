package com.example.supun.food;

import com.orm.SugarRecord;

/**
 * Created by asela on 6/15/17.
 */
public class ActiveOrderItem extends SugarRecord<ActiveOrderItem> {
    String itemName;
    String itemId;
    String qty;
    String state;
    int tableId;
    int orderId;
    double price;

    public ActiveOrderItem(String itemName, String qty, String state, int tableId, int orderId,String itemId,double price) {
        this.itemName = itemName;
        this.qty = qty;
        this.state = state;
        this.tableId = tableId;
        this.orderId = orderId;
        this.itemId = itemId;
        this.price = price;
    }

    public ActiveOrderItem(){}
}
