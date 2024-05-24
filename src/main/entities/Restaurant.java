package main.entities;

import java.util.ArrayList;
import java.util.Iterator;

public class Restaurant {
    private ArrayList<Table> orderList;

    public Restaurant() {
        orderList = new ArrayList <> ();
    }

    public ArrayList<Table> getOrderList() {
        return orderList;
    }

    public void addTable(Table table) {
        this.orderList.add(table);
    }

    public Integer calculateTotalOrders() {
        Integer totalOrders =  orderList.size();
        return totalOrders;
    }
    public Integer calculateTotalItemsOrdered(){
        Integer totalItemsOrdered = 0;

        Iterator<Table> it = orderList.iterator();
        while (it.hasNext()) {
            Table table = it.next();

            totalItemsOrdered += table.calculateTotalItemsOrdered();
        }
        return totalItemsOrdered;
    }
    public Integer calculateItemFrequency(Item item){
        Integer itemFrequency = 0;

        Iterator<Table> it = orderList.iterator();
        while (it.hasNext()) {
            Table table = it.next();

            itemFrequency += table.calculateItemFrequency(item);
        }
        return itemFrequency;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "orderList=" + orderList +
                '}';
    }
}
