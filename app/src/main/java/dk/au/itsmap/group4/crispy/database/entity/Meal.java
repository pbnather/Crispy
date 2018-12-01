package dk.au.itsmap.group4.crispy.database.entity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;
import java.util.Date;

import dk.au.itsmap.group4.crispy.model.IMeal;

public class Meal extends Entity implements IMeal {

    private String title;
    private String image_url;
    private String cookName;
    private DocumentReference recipe;
    private Timestamp date;

    public Meal() {}

    public DocumentReference getRecipe() {
        return recipe;
    }

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
    public String getCookName() {
        return cookName;
    }

    @Override
    public String getRecipeId() {
        return recipe.getId();
    }

    @Override
    public Date getDate() {
        return date != null ? date.toDate() : null;
    }

    @Override
    public int getDateHours() {
        if(getDate() == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate());
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    @Override
    public int getDateMinutes() {
        if(getDate() == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate());
        return cal.get(Calendar.MINUTE);
    }


}
