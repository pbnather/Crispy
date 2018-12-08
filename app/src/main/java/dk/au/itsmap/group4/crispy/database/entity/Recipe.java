package dk.au.itsmap.group4.crispy.database.entity;

import com.google.firebase.firestore.Exclude;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import dk.au.itsmap.group4.crispy.model.IRecipe;

public class Recipe extends Entity implements IRecipe {

    private String title;
    private String image_url;
    private String description;

    public Recipe() {}

    public Recipe(String id, String title, String image_url, String description) {
        this.id = id;
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

    @Override
    public void setImage_url(String url) {
        this.image_url = url;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    @Override
    public java.lang.String toString() {
        return title;
    }
}
