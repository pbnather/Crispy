package dk.au.itsmap.group4.crispy.database;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.List;

import androidx.lifecycle.LiveData;
import dk.au.itsmap.group4.crispy.database.entity.Ingredient;
import dk.au.itsmap.group4.crispy.database.entity.Meal;
import dk.au.itsmap.group4.crispy.database.entity.Recipe;
import dk.au.itsmap.group4.crispy.model.IIngredient;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.model.IRepository;

public class FSRepository implements IRepository {

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
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);

        // Get references to all relevant collections
        mRecipes = firestore.collection("recipes");
        mMeals = firestore.collection("meals");
    }

    @Override
    public LiveData<IMeal> getMealById(String id) {
        DocumentReference docRef = mMeals.document(id);
        return new FSLiveData<Meal, IMeal>(docRef, Meal.class);
    }

    @Override
    public LiveData<IRecipe> getRecipeById(String id) {
        DocumentReference docRef = mRecipes.document(id);
        return new FSLiveData<Recipe, IRecipe>(docRef, Recipe.class);
    }

    @Override
    public LiveData<IRecipe> getRecipeForMeal(IMeal meal) {
        DocumentReference docRef = mRecipes.document(meal.getRecipeId());
        return new FSLiveData<Recipe, IRecipe>(docRef, Recipe.class);
    }

    @Override
    public LiveData<List<IMeal>> getAllMeals() {
        return new FSLiveDataList<Meal, IMeal>(mMeals, Meal.class);
    }

    @Override
    public LiveData<List<IRecipe>> getAllRecipes() {
        return new FSLiveDataList<Recipe, IRecipe>(mRecipes, Recipe.class);
    }

    @Override
    public LiveData<List<IIngredient>> getIngredientsForRecipe(IRecipe recipe) {
        CollectionReference ingredients = mRecipes.document(recipe.getId()).collection("ingredients");
        return new FSLiveDataList<Ingredient, IIngredient>(ingredients, Ingredient.class);
    }

    @Override
    public LiveData<List<IIngredient>> getIngredientsForRecipeById(String recipeId) {
        CollectionReference ingredients = mRecipes.document(recipeId).collection("ingredients");
        return new FSLiveDataList<Ingredient, IIngredient>(ingredients, Ingredient.class);
    }

//    @Override
//    public void saveRecipe(IRecipe recipe, List<IIngredient> ingredients) {
//
//    }
//
//    @Override
//    public LiveData<List<? extends IIngredient>> getIngredientsForRecipe(IRecipe recipe) {
//        return null;
//    }
//
//    @Override
//    public void saveRecipe(IRecipe recipe,  List<IIngredient> ingredients) {
//        mRecipes
//                .add(recipe)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Log.i("SUCCESS", "Firestore rocks!");
//                    } else {
//                        Log.e("ERROR", "Getting recipe from Firestore is hard! :o");
//                    }
//                });
//    }
//
//    @Override
//    public void saveMeal(IMeal meal) {
//        mMeals
//                .add(meal)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Log.i("SUCCESS", "Firestore rocks!");
//                    } else {
//                        Log.e("ERROR", "Getting recipe from Firestore is hard! :o");
//                    }
//                });
//    }

}
