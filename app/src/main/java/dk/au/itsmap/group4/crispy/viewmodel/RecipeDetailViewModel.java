package dk.au.itsmap.group4.crispy.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import dk.au.itsmap.group4.crispy.database.FSRepository;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.model.IRepository;

public class RecipeDetailViewModel extends AndroidViewModel {

    private IRepository mRepository;
    private LiveData<List<IRecipe>> mRecipes;
    private LiveData<IRecipe> mRecipe;

    public RecipeDetailViewModel(@NonNull Application application) {
        super(application);

        mRepository = FSRepository.getInstance();
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

}
