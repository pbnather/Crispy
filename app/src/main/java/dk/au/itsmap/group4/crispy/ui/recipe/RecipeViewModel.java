package dk.au.itsmap.group4.crispy.ui.recipe;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import dk.au.itsmap.group4.crispy.database.FSRepository;
import dk.au.itsmap.group4.crispy.database.entity.Ingredient;
import dk.au.itsmap.group4.crispy.model.IIngredient;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.model.IRepository;

/**
 * ViewModel shared between RecipeList and RecipeDetail Activity
 */
public class RecipeViewModel extends AndroidViewModel {

    private IRepository mRepository;
    private LiveData<List<IIngredient>> mSelectedRecipeIngredients;
    private LiveData<List<IRecipe>> mRecipes;
    private LiveData<IRecipe> mSelectedRecipe;
    private String mSelectedRecipeId;

    public RecipeViewModel(@NonNull Application application) {
        super(application);

        mRepository = FSRepository.getInstance();
    }

    public LiveData<List<IRecipe>> getAllRecipes() {
        if (mRecipes == null) {
            mRecipes = mRepository.getAllRecipes();
        }
        return mRecipes;
    }

    public LiveData<List<IIngredient>> getIngredientsForSelectedRecipe() {
        if (mSelectedRecipeIngredients == null) {
            mSelectedRecipeIngredients = new LiveData<List<IIngredient>>() {};
        }
        return mSelectedRecipeIngredients;
    }

    public LiveData<IRecipe> getSelectedRecipe() {
        if (mSelectedRecipe == null) {
            mSelectedRecipe = new LiveData<IRecipe>() {};
        }
        return mSelectedRecipe;
    }

    public void selectRecipe(String recipeId) {
        if(mSelectedRecipeId == null || !mSelectedRecipeId.equals(recipeId)) {
            mSelectedRecipeId = recipeId;
            mSelectedRecipe = mRepository.getRecipeById(recipeId);
            mSelectedRecipeIngredients = mRepository.getIngredientsForRecipeById(recipeId);
        }
    }

    public void saveRecipe(IRecipe recipe, List<IIngredient> added, List<IIngredient> deleted){
        mRepository.deleteIngredientsForRecipe(recipe, deleted);
        mRepository.saveRecipeWithIngredients(recipe, added);
    }
}
