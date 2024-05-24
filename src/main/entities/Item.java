package main.entities;

import java.util.ArrayList;

public class Item {
    private Integer itemID;
    private String name;
    private Double price;
    private Integer calories;
    private String description;
    private ArrayList<ItemType> types;
    private Course course;
    private String imageLocation;

    public enum Course {STARTER, MAIN, DESSERT, DRINK}
    public enum ItemType {VEGETARIAN, GLUTEN_FREE, DAIRY_FREE,}

    public Item() {
    }
    public Item(Integer itemID, String name, Double price, Integer calories, String description) {
        this.itemID = itemID;
        this.name = name;
        this.price = price;
        this.calories = calories;
        this.description = description;
    }
    public Item(Integer itemID, String name, Double price, Integer calories, String description,Course course, ArrayList<ItemType> types) {
        this.itemID = itemID;
        this.name = name;
        this.price = price;
        this.calories = calories;
        this.description = description;
        this.course = course;
        this.types = types;
    }
    public Item(Integer itemID, String name, Double price, Integer calories, String description, Course course, ArrayList<ItemType> types, String imageLocation) {
        this.itemID = itemID;
        this.name = name;
        this.price = price;
        this.calories = calories;
        this.description = description;
        this.types = types;
        this.course = course;
        this.imageLocation = imageLocation;
    }

    public Integer getItemID() {
        return itemID;
    }
    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public Integer getCalories() {
        return calories;
    }
    public void setCalories(Integer calories) {
        this.calories = calories;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public ArrayList<ItemType> getTypes() {
        return types;
    }
    public void setTypes(ArrayList<ItemType> types) {
        this.types = types;
    }
    public Course getCourse() {
        return course;
    }
    public void setCourse(Course course) {
        this.course = course;
    }
    public String getImageLocation() {
        return imageLocation;
    }
    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemID=" + itemID +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", calories=" + calories +
                ", description='" + description + '\'' +
                ", types=" + types +
                ", course=" + course +
                ", imageLocation='" + imageLocation + '\'' +
                '}';
    }
}
