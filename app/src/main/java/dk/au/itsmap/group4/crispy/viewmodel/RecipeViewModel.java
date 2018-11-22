package dk.au.itsmap.group4.crispy.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import dk.au.itsmap.group4.crispy.Repository;
import dk.au.itsmap.group4.crispy.database.entity.Recipe;

public class RecipeViewModel extends AndroidViewModel {

    private Repository mRepository;
    private LiveData<List<Recipe>> mRecipes;
    private MutableLiveData<Recipe> selectedRecipe = new MutableLiveData<>();

    public RecipeViewModel(@NonNull Application application) {
        super(application);

        mRepository = Repository.getInstance(application);

    }

    public LiveData<List<Recipe>> getRecipes() {
        if (mRecipes == null) {
            mRecipes = mRepository.getRecipes();
        }
        return mRecipes;
    }

    public LiveData<Recipe> getRecipe(int id) {
        return mRepository.getRecipe(id);
    }

    public LiveData<Recipe> getSelectedRecipe() {
        return selectedRecipe;
    }

    public void setSelectedRecipe(Recipe recipe) {
        this.selectedRecipe.setValue(recipe);
    }
}
