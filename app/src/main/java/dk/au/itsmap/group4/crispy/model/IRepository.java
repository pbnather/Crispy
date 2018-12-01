package dk.au.itsmap.group4.crispy.model;

import java.util.List;

import androidx.lifecycle.LiveData;

public interface IRepository {

    LiveData<IMeal> getMealById(String id);
    LiveData<IRecipe> getRecipeById(String id);
    LiveData<IRecipe> getRecipeForMeal(IMeal meal);

    LiveData<List<IMeal>> getAllMeals();
    LiveData<List<IRecipe>> getAllRecipes();
    LiveData<List<IIngredient>> getIngredientsForRecipe(IRecipe recipe);
    LiveData<List<IIngredient>> getIngredientsForRecipeById(String recipeId);

//    void saveMeal(IMeal meal);
//    void saveRecipe(IRecipe recipe);
//    void saveRecipe(String recipeId, List<IIngredient> ingredients);
//    void saveRecipe(IRecipe recipe, List<IIngredient> ingredients);

    // TODO: delete meals, recipes and ingredients
}
