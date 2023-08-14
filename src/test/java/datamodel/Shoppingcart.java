package datamodel;

public class Shoppingcart {
    int id;
    String title;


    double cups;
    int gramsPerCup;


    public int getId() {return id;}
    public void setId(int Id) { this.id = id;}


    public String getTitle() {return title;}
    public void setTitle( String title){ this.title = title;}





    public double getCups() {return cups;}
    public void setCups( double cups) {  this.cups = cups;}

    public int getGramsPerCup() {return gramsPerCup;}
    public void setGramsPerCup( int gramsPerCup){  this.gramsPerCup = gramsPerCup;}

    public double getTotalGrams() {
        return cups * gramsPerCup;
    }


    @Override
    public String toString() {
        return "ID: " + id + "\nTitle: " + title +
                "\nCUPS(1 cup eq to 100 gm): " + cups + "\nGRAMS: " + getTotalGrams() + "\n";
    }
}

