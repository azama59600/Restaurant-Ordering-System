package main.entities;

import java.util.*;

public class Menu {
    private ArrayList<Item> itemList;

    public Menu() {
        itemList = new ArrayList <> ();
    }

    public ArrayList<Item> getItemList() {
        return itemList;
    }
    public void setItemList(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }

    public void addItem (Item i) {
        this.itemList.add(i);
    }
    public void removeItem (Item i) {
        this.itemList.remove(i);
    }

    public void sortMenu(String sortBy, String orderBy){
        if(orderBy!=null && !orderBy.isEmpty()) {
            if (sortBy.equals("Name")) {
                System.out.println("name");
                this.itemList.sort(Comparator.comparing(Item::getName));
            } else if (sortBy.equals("Calories")) {
                this.itemList.sort(Comparator.comparing(Item::getCalories));
            } else if (sortBy.equals("Price")) {
                this.itemList.sort(Comparator.comparing(Item::getPrice));
            }

            if (orderBy.equals("DESC")){
                Collections.reverse(this.itemList);
            }
        }
    }
    public ArrayList<Item> filterCourseTypeItems(Item.Course courseType){
        ArrayList courseTypeItems = new ArrayList<Item>();

        Iterator<Item> it = itemList.iterator();
        while (it.hasNext()){
            Item item = it.next();
            if (item.getCourse().equals(courseType)){
                courseTypeItems.add(item);
            }
        }
        return courseTypeItems;
    }

}
