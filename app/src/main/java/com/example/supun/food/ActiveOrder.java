package com.example.supun.food;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asela on 6/15/17.
 */
public class ActiveOrder extends SugarRecord<ActiveOrder>  {
    String tableId;

    boolean isCompleted;
    @Ignore
    List<ActiveOrderItem> itemList;


    public ActiveOrder(){}
    public ActiveOrder(String tableId) {
        this.tableId = tableId;
        this.isCompleted = false;
        this.itemList = new ArrayList<ActiveOrderItem>();

    }
    public List<ActiveOrderItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<ActiveOrderItem> itemList) {
        this.itemList = itemList;
    }
    public void changeState(String id,String state){
        for(ActiveOrderItem item:itemList){
            Log.d("active_order","item id"+id);
            if(item.itemId.equals(id)){
                item.state = state;
                Log.d("active_order","state changed: "+id+" "+state);
                return;
            }
        }
    }

}
