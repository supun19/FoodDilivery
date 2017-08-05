package com.example.supun.food;

import android.content.ClipData;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by asela on 5/17/17.
 */
public class Order {
//    private static Order instance;

//    private HashMap<String,Integer> orderedItems;

    private HashMap<String,OrderedItem> orderedItemList;

    private String tableId;

    public Order(){
//        orderedItems = new HashMap<>();
        orderedItemList = new HashMap<>();
    }


    public int addItem(String itemName){
        if(orderedItemList.containsKey(itemName)){
            return orderedItemList.get(itemName).addOne();
        }
        else{
            MenuItem menuItem =MenuItem.findWithQuery(MenuItem.class,"SELECT * from MENU_ITEM Where name='"+itemName+"'").get(0);
            //userId~tableId~timestamp~localOrderItemId~PreparedInId
            String itemId = GlobalState.getCurrrentUserId()+"."+tableId+"."+System.currentTimeMillis()+"."+GlobalState.orderedItemCount+"."+menuItem.prepearedIn;

            OrderedItem item = new OrderedItem(menuItem.menuId,menuItem.category,menuItem.name,menuItem.price,menuItem.imageName,menuItem.prepearedIn,itemId,1);
            GlobalState.orderedItemCount++;
            orderedItemList.put(itemName,item);
            return 1;
        }

//        if(orderedItems.containsKey(item)){
//            orderedItems.put(item,orderedItems.get(item)+1);
//        }
//        else
//            orderedItems.put(item,1);
//        return orderedItems.get(item);
    }

    public int removeItem(String itemName){
        if(orderedItemList.containsKey(itemName)){
            int newCount = orderedItemList.get(itemName).removeOne();
            if(newCount==0){
                orderedItemList.remove(itemName);
            }
            return newCount;
        }
        else{

            return -1;
        }
//        if(orderedItems.containsKey(item)){
//            int newCount = orderedItems.get(item)-1;
//            if(newCount == 0){
//                orderedItems.remove(item);
//            }
//            else {
//                orderedItems.put(item, newCount);
//            }
//            return newCount;
//        }
//        return -1;
    }
    public boolean setOption(String itemName,String option,Double price){
        if(orderedItemList.containsKey(itemName)){
            orderedItemList.get(itemName).price = price;
            orderedItemList.get(itemName).name = itemName+" "+option;
            Log.d("order_setOption","new name:"+orderedItemList.get(itemName).name );
            return true;
        }
//        else{
//            MenuItem menuItem =MenuItem.findWithQuery(MenuItem.class,"SELECT * from MENU_ITEM Where name='"+itemName+"'").get(0);
//            //userId~tableId~timestamp~localOrderItemId~PreparedInId
//            String itemId = GlobalState.getCurrrentUserId()+"."+tableId+"."+System.currentTimeMillis()+"."+GlobalState.orderedItemCount+"."+menuItem.prepearedIn;
//
//            OrderedItem item = new OrderedItem(menuItem.menuId,menuItem.category,itemName,price,menuItem.imageName,menuItem.prepearedIn,itemId,0);
//           // GlobalState.orderedItemCount++;
//            orderedItemList.put(itemName,item);
//        }
        Log.d("order_setOption","item not found");
        return false;

    }

    public HashMap<String,OrderedItem> getOrder(){

        return orderedItemList;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

//    public ArrayList<OrderedItem> getOrderedItemList() {
//        return orderedItemList;
//    }

}
