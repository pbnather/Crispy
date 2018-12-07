package dk.au.itsmap.group4.crispy.service.foodapi;

public class RecipeImage {

    private String id;
    private String title;
    private String image;

    public RecipeImage(String id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image.replaceFirst("312x231.jpg", "636x393.jpg");
    }

    public void setImage(String image) {
        this.image = image;
    }
}
