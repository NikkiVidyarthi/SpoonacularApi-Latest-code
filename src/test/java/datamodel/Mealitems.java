package datamodel;

public class Mealitems {
    int day;
    int slot;
    int position;
    String type;
    Recipe value;

    public int getDay() { return day; }
    public void setDay(int day) {this.day = day;  }

    public int getSlot() { return slot;}
    public void setSlot(int slot) {this.slot = slot;}


    public int getPosition() { return position;  }
    public void setPosition(int position) {    this.position = position;}


    public String getType() { return type; }
    public void setType(String type) {  this.type = type; }


    public Recipe getValue() {return value; }
    public void setValue(Recipe value) { this.value = value;}


}