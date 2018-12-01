package dk.au.itsmap.group4.crispy.viewmodel;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import dk.au.itsmap.group4.crispy.database.FSRepository;
import dk.au.itsmap.group4.crispy.database.entity.Ingredient;
import dk.au.itsmap.group4.crispy.database.entity.Recipe;
import dk.au.itsmap.group4.crispy.model.IIngredient;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.model.IRepository;

public class RecipeDetailViewModel extends AndroidViewModel {

    private IRepository mRepository;
    private LiveData<List<IRecipe>> mRecipes;
    private LiveData<IRecipe> mRecipe;
    private LiveData<List<IIngredient>> mIngredients;

    public RecipeDetailViewModel(@NonNull Application application) {
        super(application);

        mRepository = FSRepository.getInstance();
//        IRecipe recipe = new Recipe("Pasta with Cucumber 2", "Test.url", "Desc");
//        List<IIngredient> ingredients = new ArrayList<>();
//        ingredients.add(new Ingredient("Cucumber", "pc", 5));
//        ingredients.add(new Ingredient("Pasta", "g", 125));
//        ingredients.add(new Ingredient("Pesto Rosso", "g", 30));
//        String id = mRepository.saveRecipeWithIngredients(recipe, ingredients);

    }

    public LiveData<List<IRecipe>> getRecipes() {
        if (mRecipes == null) {
            mRecipes = mRepository.getAllRecipes();
        }
        return mRecipes;
    }

    public LiveData<IRecipe> getRecipe(String id) {
        if (mRecipe == null) {
            mRecipe = mRepository.getRecipeById(id);
        }
        return mRecipe;
    }

    public LiveData<List<IIngredient>> getIngredientsForRecipeById(String id) {
        if (mIngredients == null) {
            mIngredients = mRepository.getIngredientsForRecipeById(id);
        }
        return mIngredients;
    }

    public void deleteRecipe(IRecipe recipe) {
        mRepository.deleteRecipe(recipe);
    }

    public void deleteIngredientsForRecipeById(String id, List<IIngredient> ingredients) {
        mRepository.deleteIngredientsForRecipeById(id, ingredients);
    }

}
