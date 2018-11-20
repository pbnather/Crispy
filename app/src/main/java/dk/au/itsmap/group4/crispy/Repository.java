package dk.au.itsmap.group4.crispy;

import java.util.List;

import androidx.lifecycle.LiveData;
import dk.au.itsmap.group4.crispy.database.entity.Meal;
import dk.au.itsmap.group4.crispy.database.entity.Recipe;

public class Repository {

    // Public interface

    public LiveData<List<Recipe>> getRecipes() {
        return null;
    }

    public LiveData<Recipe> getRecipe(int id) {
        return null;
    }

    public LiveData<List<Meal>> getMeals() {
        return null;
    }

    public LiveData<Meal> getMeal(int id) {
        return null;
    }
}
