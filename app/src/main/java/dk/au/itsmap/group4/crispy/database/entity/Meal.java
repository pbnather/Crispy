package dk.au.itsmap.group4.crispy.database.entity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.model.IRecipe;

public class Meal extends Entity implements IMeal {

    private String title;
    private String image_url;
    private String cookImage;
    private String cookName;
    private String recipeId;
    private Timestamp date;

    public Meal() {
        this.date = new Timestamp(new Date());
    }

    public Meal(String title, String image_url, String cookName, String recipeId, Date date) {
        this.title = title;
        this.image_url = image_url;
        this.cookName = cookName;
        this.recipeId = recipeId;
        this.date = new Timestamp(date);
    }

    public Meal(@NonNull IRecipe recipe, String cookName, Date date) {
        this.title = recipe.getTitle();
        this.image_url = recipe.getImage_url();
        this.cookName = cookName;
        this.recipeId = recipe.getId();
        this.date = new Timestamp(date);
    }

    @Override
    public String getRecipeId() {
        return recipeId;
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
    public String getCookName() {
        return cookName;
    }

    @Override
    public String getCookImage(){ return  cookImage;}

    @Override
    public Date getDate() {
        return date != null ? date.toDate() : null;
    }

    @Override
    public int getDateHours() {
        return date == null ? 0 : time(Calendar.HOUR_OF_DAY);
    }

    @Override
    public int getDateMinutes() {
        return date == null ? 0 : time(Calendar.MINUTE);
    }

    public Calendar getCalendarInstance() {
        Calendar c = Calendar.getInstance();
        c.setTime(date.toDate());
        return c;
    }

    private int time(int time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate());
        return cal.get(time);
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setCookName(String cookName) {
        this.cookName = cookName;
    }

    @Override
    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public void setImage_url(String imageUrl) {
        this.image_url = imageUrl;
    }

    @Override
    public void setCookImage(String image_url) {
        cookImage = image_url;
    }

    @Override
    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

}
