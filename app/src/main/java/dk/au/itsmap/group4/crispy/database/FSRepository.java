package dk.au.itsmap.group4.crispy.database;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

import androidx.lifecycle.LiveData;
import dk.au.itsmap.group4.crispy.database.entity.Ingredient;
import dk.au.itsmap.group4.crispy.database.entity.Meal;
import dk.au.itsmap.group4.crispy.database.entity.Recipe;
import dk.au.itsmap.group4.crispy.model.Repository;

public class FSRepository implements Repository {

    private CollectionReference mRecipes;
    private CollectionReference mMeals;

    /* adapted from BasicSample demo */
    private static FSRepository sInstance;
    public static FSRepository getInstance() {
        if (sInstance == null) {
            synchronized (FSRepository.class) {
                if (sInstance == null) {
                    sInstance = new FSRepository();
                }
            }
        }
        return sInstance;
    }

    private FSRepository() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        mRecipes = firestore.collection("recipes");
        mMeals = firestore.collection("meals");
    }

    @Override
    public LiveData<Recipe> getRecipe(String id) {
        return null;
    }

    @Override
    public LiveData<Recipe> getRecipe(Meal meal) {
        return null;
    }

    @Override
    public LiveData<List<Meal>> getMeals() {
        return null;
    }

    @Override
    public LiveData<List<Ingredient>> getIngredientsForRecipe(Recipe recipe) {
        return null;
    }

    @Override
    public void saveRecipe(Recipe recipe, List<Ingredient> ingredients) {
        mRecipes
                .add(recipe)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i("SUCCESS", "Firestore rocks!");
                    } else {
                        Log.e("ERROR", "Getting recipe from Firestore is hard! :o");
                    }
                });
    }

    @Override
    public void saveMeal(Meal meal) {
        mMeals
                .add(meal)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i("SUCCESS", "Firestore rocks!");
                    } else {
                        Log.e("ERROR", "Getting recipe from Firestore is hard! :o");
                    }
                });
    }

    @Override
    public FSLiveDataList<Recipe> getRecipes() {
        return new FSLiveDataList<Recipe>(mRecipes, Recipe.class);
    }

}
