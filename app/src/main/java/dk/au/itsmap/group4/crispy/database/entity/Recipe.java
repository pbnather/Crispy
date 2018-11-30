package dk.au.itsmap.group4.crispy.database.entity;

public class Recipe {

    private String title;
    private String description;
    private String image_url;

    public Recipe() {}

    public Recipe(String title, String description, String image_url) {
        this.title = title;
        this.description = description;
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage_url() {
        return image_url;
    }
}
