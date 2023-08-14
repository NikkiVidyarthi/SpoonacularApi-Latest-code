package datamodel;

public class Recipe {
    int id;
    String servings;
    String title;
    String imageType;

    public int getId() { return id;}
    public void setId(int id) { this.id = id;}
    public String getServings() { return servings;}
    public  void setServings( String servings) { this.servings = servings;}

    public String getTitle() { return title;}
    public  void  setTitle( String title) { this.title = title;}
    public String getImageType() { return imageType;}
    public void setImageType( String imageType) { this.imageType = imageType;}



}
