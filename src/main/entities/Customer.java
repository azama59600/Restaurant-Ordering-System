package main.entities;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Iterator;

public class Customer {
    private Integer customerID;
    private String name;
    private ArrayList <Item> meal;

    public Customer() {
        meal = new ArrayList <> ();
    }
    public Customer(Integer customerID) {
        this.customerID = customerID;
        meal = new ArrayList <> ();
    }

    public Integer getCustomerID() {
        return customerID;
    }
    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<Item> getMeal() {
        return meal;
    }
    public void setMeal(ArrayList<Item> meal) {
        this.meal = meal;
    }

    public void addItem (Item i) {
        this.meal.add(i);
    }
    public void removeItem (Item i) {
        this.meal.remove(i);
    }

    public Double calculateMealPrice() {
        double price = 0.0;

        for (Item i : this.meal) {
            price += i.getPrice ();
        }

        return price;
    }
    public Integer calculateMealCalories() {
        Integer calories = 0;

        for (Item i : this.meal) {
            calories += i.getCalories ();
        }

        return calories;
    }
    public Integer calculateItemFrequency(Item item) {
        Integer itemFrequency = 0;
        Iterator <Item> it = meal.iterator();
        while (it.hasNext()) {
            Item i = it.next();
            if (i.toString().contains(item.toString())) {
                itemFrequency += 1;
            }
        }
        return itemFrequency;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerID=" + customerID +
                ", name='" + name + '\'' +
                ", meal=" + meal +
                '}';
    }
}
