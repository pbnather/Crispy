package dk.au.itsmap.group4.crispy.model;

import java.util.List;

import androidx.lifecycle.LiveData;
import dk.au.itsmap.group4.crispy.database.entity.Ingredient;
import dk.au.itsmap.group4.crispy.database.entity.Meal;
import dk.au.itsmap.group4.crispy.database.entity.Recipe;

public interface Repository {

    LiveData<Recipe> getRecipe(String id);
    LiveData<Recipe> getRecipe(Meal meal);

    LiveData<List<Meal>> getMeals();
    LiveData<List<Recipe>> getRecipes();
    LiveData<List<Ingredient>> getIngredientsForRecipe(Recipe recipe);

    void saveRecipe(Recipe recipe, List<Ingredient> ingredients);
    void saveMeal(Meal meal);
}
