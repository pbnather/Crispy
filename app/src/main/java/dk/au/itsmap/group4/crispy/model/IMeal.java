package dk.au.itsmap.group4.crispy.model;

import java.util.Date;

public interface IMeal {

    String getId();

    String getTitle();

    String getImage_url();

    String getRecipeId();

    String getCookName();

    Date getDate();

    int getDateHours();

    int getDateMinutes();

}
