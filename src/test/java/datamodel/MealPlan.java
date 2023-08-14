package datamodel;

import java.util.List;

public class MealPlan {
    String name;
    List<Mealitems> items;


    public String getName() { return name;}
    public void setName(String name) {this.name = name;}

    public List<Mealitems> getItems() { return items;}
    public void setItems(List<Mealitems> items) { this.items = items;}

    }


