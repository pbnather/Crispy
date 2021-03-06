package dk.au.itsmap.group4.crispy.database.entity;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

/* Inspired by https://stackoverflow.com/a/46999773 */
@IgnoreExtraProperties
public abstract class Entity {

    @Exclude protected String id;

    public void setId(String id) {
        this.id = id;
    }

}
