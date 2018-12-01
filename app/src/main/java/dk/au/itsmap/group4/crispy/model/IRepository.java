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

    String saveMeal(IMeal meal);
    String saveRecipe(IRecipe recipe);
    String saveIngredientsForRecipe(IRecipe recipe, List<IIngredient> ingredients);
    String saveIngredientsForRecipeById(String recipeId, List<IIngredient> ingredients);
    String saveRecipeWithIngredients(IRecipe recipe, List<IIngredient> ingredients);

    void deleteRecipe(IRecipe recipe);
    void deleteRecipe(String recipeId);
    void deleteMeal(IMeal meal);
    void deleteMeal(String mealId);
    void deleteIngredientsForRecipe(IRecipe recipe, List<IIngredient> ingredients);
    void deleteIngredientsForRecipeById(String recipeId, List<IIngredient> ingredients);
}
