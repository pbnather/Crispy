package dk.au.itsmap.group4.crispy.database.entity;

import com.google.firebase.firestore.Exclude;

import dk.au.itsmap.group4.crispy.model.IRecipe;

public class Recipe extends Entity implements IRecipe {

    private String title;
    private String image_url;
    private String description;

    public Recipe() {}

    public Recipe(String title, String image_url, String description) {
        this.title = title;
        this.image_url = image_url;
        this.description = description;
    }

    @Override
    @Exclude
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getImage_url() {
        return image_url;
    }

    @Override
    public String getDescription() {
        return description;
    }



}
