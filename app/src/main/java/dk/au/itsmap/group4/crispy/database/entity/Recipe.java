package dk.au.itsmap.group4.crispy.database.entity;

import dk.au.itsmap.group4.crispy.model.IRecipe;

public class Recipe extends Entity implements IRecipe {

    private String title;
    private String image_url;
    private String description;

    public Recipe() {}

    @Override
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
