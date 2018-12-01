package dk.au.itsmap.group4.crispy.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import dk.au.itsmap.group4.crispy.database.FSRepository;
import dk.au.itsmap.group4.crispy.database.entity.Recipe;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.model.IRepository;

public class RecipeViewModel extends AndroidViewModel {

    private IRepository mRepository;
    private LiveData<List<IRecipe>> mRecipes;
    private MutableLiveData<IRecipe> selectedRecipe = new MutableLiveData<>();

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

    public LiveData<IRecipe> getRecipe(String id) {
        return mRepository.getRecipeById(id);
    }

    public LiveData<IRecipe> getSelectedRecipe() {
        return selectedRecipe;
    }

    public void setSelectedRecipe(Recipe recipe) {
        this.selectedRecipe.setValue(recipe);
    }
}
