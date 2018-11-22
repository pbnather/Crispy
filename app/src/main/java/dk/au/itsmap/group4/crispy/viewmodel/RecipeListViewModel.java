package dk.au.itsmap.group4.crispy.viewmodel;

import android.app.Application;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import dk.au.itsmap.group4.crispy.Repository;
import dk.au.itsmap.group4.crispy.database.entity.Ingredient;
import dk.au.itsmap.group4.crispy.database.entity.Product;
import dk.au.itsmap.group4.crispy.database.entity.Recipe;
import dk.au.itsmap.group4.crispy.database.entity.Unit;

public class RecipeListViewModel extends AndroidViewModel {

    private Repository mRepository;
    private LiveData<List<Recipe>> mRecipes;

    public RecipeListViewModel(@NonNull Application application) {
        super(application);

        mRepository = Repository.getInstance(application);

    }

    public LiveData<List<Recipe>> getRecipes() {
        if (mRecipes == null) {
            mRecipes = mRepository.getRecipes();
        }
        return mRecipes;
    }

}
