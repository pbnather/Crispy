package dk.au.itsmap.group4.crispy.database.entity;

import com.google.firebase.Timestamp;

public class Meal {

    private String title;
    private String image_url;
    private String recipe;
    private Timestamp time;

    public Meal() {}

    public String getTitle() {
        return title;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getRecipe() {
        return recipe;
    }

    public Timestamp getTime() {
        return time;
    }
}
