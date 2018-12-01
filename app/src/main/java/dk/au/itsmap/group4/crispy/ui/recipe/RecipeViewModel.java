package dk.au.itsmap.group4.crispy.ui.recipe;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import dk.au.itsmap.group4.crispy.database.FSRepository;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.model.IRepository;

/**
 * ViewModel shared between RecipeList and RecipeDetail Activity
 */
public class RecipeViewModel extends AndroidViewModel {

    private IRepository mRepository;
    private LiveData<List<IRecipe>> mRecipes;
    private LiveData<IRecipe> selectedRecipe;

    public RecipeViewModel(@NonNull Application application) {
        super(application);

        mRepository = FSRepository.getInstance();

    }

    public LiveData<List<IRecipe>> getRecipes() {
        if (mRecipes == null) {
            mRecipes = mRepository.getAllRecipes();
        }
        return mRecipes;
    }

    public LiveData<IRecipe> getRecipeById(String id) {
        return mRepository.getRecipeById(id);
    }

    public LiveData<IRecipe> getSelectedRecipe() {
        return selectedRecipe;
    }

    public void setSelectedRecipe(String recipeId) {
        this.selectedRecipe = mRepository.getRecipeById(recipeId);
    }
}
