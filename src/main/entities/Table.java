package main.entities;

import java.util.ArrayList;
import java.util.Iterator;

public class Table {
    private String tableID;
    private int dinerNum;
    private ArrayList <Customer> customers;

    public Table() {
        customers = new ArrayList <> ();
    }
    public Table(String tableID, int dinerNum) {
        this.tableID = tableID;
        this.dinerNum = dinerNum;
        customers = new ArrayList <> ();
    }

    public String getTableID() {
        return tableID;
    }
    public void setTableID(String tableID) {
        this.tableID = tableID;
    }
    public int getDinerNum() {
        return dinerNum;
    }
    public void setDinerNum(int dinerNum) {
        this.dinerNum = dinerNum;
    }
    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void addCustomers(int dinerNum) {
        for(int i=0;i<dinerNum;i++){
            int a = i+1;
            this.customers.add(new Customer(a));
        }
    }

    public Boolean checkForEmptyMeals(){
        Iterator<Customer> it = customers.iterator();
        while (it.hasNext()) {
            Customer customer = it.next();
            ArrayList<Item> meal = customer.getMeal();
            if (meal.isEmpty()){
                return true;
            }
        }
        return false;
    }

    public Double calculateOrderCost() {
        Double totalCost = 0.0;
        Iterator<Customer> it = customers.iterator();
        while (it.hasNext()) {
            Customer customer = it.next();
            totalCost = totalCost + customer.calculateMealPrice();
        }
        return totalCost;
    }
    public Integer calculateOrderCalories() {
        Integer totalCalories = 0;
        Iterator<Customer> it = customers.iterator();
        while (it.hasNext()) {
            Customer customer = it.next();
            totalCalories = totalCalories + customer.calculateMealCalories();
        }
        return totalCalories;
    }
    public Integer calculateTotalItemsOrdered(){
        Integer totalItemsOrdered = 0;
        Iterator<Customer> it = customers.iterator();
        while (it.hasNext()) {
            Customer customer = it.next();
            totalItemsOrdered += customer.getMeal().size();
        }
        return totalItemsOrdered;
    }
    public Integer calculateItemFrequency(Item item) {
        Integer itemFrequency = 0;

        Iterator<Customer> it = customers.iterator();
        while (it.hasNext()) {
            Customer customer = it.next();

            itemFrequency += customer.calculateItemFrequency(item);
        }
        return itemFrequency;
    }

    @Override
    public String toString() {
        return "Table{" +
                "tableID='" + tableID + '\'' +
                ", dinerNum=" + dinerNum +
                ", customers=" + customers +
                '}';
    }
}
