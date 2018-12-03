package dk.au.itsmap.group4.crispy.database;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.WriteBatch;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import dk.au.itsmap.group4.crispy.database.entity.Ingredient;
import dk.au.itsmap.group4.crispy.database.entity.Meal;
import dk.au.itsmap.group4.crispy.database.entity.Recipe;
import dk.au.itsmap.group4.crispy.model.IIngredient;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.model.IRepository;

public class FSRepository implements IRepository {

    private final String TAG = "FSRepository";

    private FirebaseFirestore mFirestore;
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
        // Set up Firestore to use a new Timestamp.class
        mFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);

        // Get references to all relevant collections
        mRecipes = mFirestore.collection("recipes");
        mMeals = mFirestore.collection("meals");
    }

    @Override
    public LiveData<IMeal> getMealById(@NonNull String mealId) {
        DocumentReference docRef = mMeals.document(mealId);
        return new FSLiveData<>(docRef, Meal.class);
    }

    @Override
    public LiveData<IRecipe> getRecipeById(@NonNull String recipeId) {
        DocumentReference docRef = mRecipes.document(recipeId);
        return new FSLiveData<>(docRef, Recipe.class);
    }

    @Override
    public LiveData<IRecipe> getRecipeForMeal(@NonNull IMeal meal) {
        return getRecipeById(meal.getRecipeId());
    }

    @Override
    public LiveData<List<IMeal>> getAllMeals() {
        return new FSLiveDataList<>(mMeals.whereGreaterThan("date", Timestamp.now()), Meal.class);
    }

    @Override
    public LiveData<List<IRecipe>> getAllRecipes() {
        return new FSLiveDataList<>(mRecipes, Recipe.class);
    }

    @Override
    public LiveData<List<IIngredient>> getIngredientsForRecipe(@NonNull IRecipe recipe) {
        return getIngredientsForRecipeById(recipe.getId());
    }

    @Override
    public LiveData<List<IIngredient>> getIngredientsForRecipeById(@NonNull String recipeId) {
        CollectionReference ingredients = mRecipes.document(recipeId).collection("ingredients");
        return new FSLiveDataList<>(ingredients, Ingredient.class);
    }

    @Override
    public String saveMeal(@NonNull IMeal meal) {
        DocumentReference mealRef = meal.getId() == null ? mMeals.document() : mMeals.document(meal.getId());
        mealRef.set(meal)
                .addOnSuccessListener(v -> Log.d(TAG, "Saved meal " + mealRef.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error saving meal", e));
        return mealRef.getId();
    }

    @Override
    public String saveRecipe(@NonNull IRecipe recipe) {
        DocumentReference recipeRef = recipe.getId() == null ? mRecipes.document() : mRecipes.document(recipe.getId());
        recipeRef.set(recipe)
                .addOnSuccessListener(v -> Log.d(TAG, "Saved recipe " + recipeRef.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error saving recipe", e));
        return recipeRef.getId();
    }

    @Override
    public String saveIngredientsForRecipe(@NonNull IRecipe recipe, List<IIngredient> ingredients) {
        return saveIngredientsForRecipeById(recipe.getId(), ingredients);
    }

    @Override
    public String saveIngredientsForRecipeById(@NonNull String recipeId, List<IIngredient> ingredients) {
        // Initialize batch
        WriteBatch batch = mFirestore.batch();
        // Get recipe reference
        DocumentReference recipeRef = mRecipes.document(recipeId);
        CollectionReference ingredientsRef = recipeRef.collection("ingredients");
        for(IIngredient ingredient : ingredients) {
            // Get ingredient reference and add it to the batch
            String id = ingredient.getId();
            DocumentReference ingredientRef = id == null ?
                    ingredientsRef.document() :
                    ingredientsRef.document(id);
            batch.set(ingredientRef, ingredient);
        }
        // Save the ingredients in one batch
        batch.commit()
                .addOnSuccessListener(v -> Log.d(TAG, "Saved ingredients for recipe " + recipeRef.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error saving ingredients", e));
        return recipeRef.getId();
    }

    @Override
    public String saveRecipeWithIngredients(@NonNull IRecipe recipe, List<IIngredient> ingredients) {
        // TODO: Test if ingredients can be null, if so change saveRecipe()
        // Initialize batch
        WriteBatch batch = mFirestore.batch();
        // Get recipe reference and add it to the batch
        DocumentReference recipeRef = recipe.getId() == null ? mRecipes.document() : mRecipes.document(recipe.getId());
        CollectionReference ingredientsRef = recipeRef.collection("ingredients");
        batch.set(recipeRef, recipe);
        for(IIngredient ingredient : ingredients) {
            // Get ingredient reference and add it to the batch
            String id = ingredient.getId();
            DocumentReference ingredientRef = id == null ?
                    ingredientsRef.document() :
                    ingredientsRef.document(id);
            batch.set(ingredientRef, ingredient);
        }
        // Save the recipe with ingredients in one batch
        batch.commit()
                .addOnSuccessListener(v -> Log.d(TAG, "Saved recipe with ingredients " + recipeRef.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error saving recipe with ingredients", e));
        return recipeRef.getId();
    }

    @Override
    public void deleteRecipe(@NonNull IRecipe recipe) {
        deleteRecipe(recipe.getId());
    }

    @Override
    public void deleteRecipe(@NonNull String recipeId) {
        // TODO: Delete also all the documents in the `ingredients` subcollection
        DocumentReference recipeRef = mRecipes.document(recipeId);
        recipeRef
                .delete()
                .addOnSuccessListener(v -> Log.d(TAG, "Deleted recipe " + recipeId))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting recipe", e));
    }

    @Override
    public void deleteMeal(@NonNull IMeal meal) {
        deleteMeal(meal.getId());
    }

    @Override
    public void deleteMeal(@NonNull String mealId) {
        DocumentReference recipeRef = mMeals.document(mealId);
        recipeRef
                .delete()
                .addOnSuccessListener(v -> Log.d(TAG, "Deleted meal " + mealId))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting meal", e));
    }

    @Override
    public void deleteIngredientsForRecipe(@NonNull IRecipe recipe, List<IIngredient> ingredients) {
        deleteIngredientsForRecipeById(recipe.getId(), ingredients);
    }

    @Override
    public void deleteIngredientsForRecipeById(@NonNull String recipeId, List<IIngredient> ingredients) {
        // Initialize batch
        WriteBatch batch = mFirestore.batch();
        // Get ingredients collection reference
        CollectionReference ingredientsRef = mRecipes.document(recipeId).collection("ingredients");
        for(IIngredient ingredient : ingredients) {
            // Get ingredient reference and add it to the batch
            String id = ingredient.getId();
            if(id != null) {
                DocumentReference ingredientRef = ingredientsRef.document(id);
                batch.delete(ingredientRef);
            }
        }
        // Delete the ingredients in one batch
        batch.commit()
                .addOnSuccessListener(v -> Log.d(TAG, "Deleted ingredients for recipe " + recipeId))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting ingredients", e));
    }

}