package dk.au.itsmap.group4.crispy.model;

import java.util.Calendar;
import java.util.Date;
import com.google.firebase.Timestamp;


public interface IMeal {

    String getId();

    String getTitle();

    String getImage_url();

    String getRecipeId();

    String getCookName();

    Date getDate();

    Calendar getCalendarInstance();

    int getDateHours();

    int getDateMinutes();

    void setTitle(String title);

    void setCookName(String cookName);

    void setDate(Timestamp date);

    void setImage_url(String imageUrl);

}
