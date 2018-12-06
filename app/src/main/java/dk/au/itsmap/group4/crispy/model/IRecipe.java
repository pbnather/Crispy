package dk.au.itsmap.group4.crispy.model;

import androidx.annotation.NonNull;

public interface IRecipe {

    String getId();

    String getTitle();

    String getImage_url();

    String getDescription();

    void setId(String id);

    void setImage_url(String url);

    @NonNull
    String toString();

}
