package dk.au.itsmap.group4.crispy.viewmodel;

import android.app.Application;
import android.util.Log;

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

public class RecipeDetailViewModel extends AndroidViewModel {

    private Repository mRepository;
    private LiveData<List<Recipe>> mRecipes;

    public RecipeDetailViewModel(@NonNull Application application) {
        super(application);

        mRepository = Repository.getInstance(application);

        //Mock Data
        Recipe recipe = new Recipe(0, "Test Title", "Short very short desc",
                Arrays.asList(new Ingredient(0, new Product(0, "Pepper"), 1.0, Unit.piece),
                              new Ingredient(0, new Product(0, "Bread"), 2.0, Unit.piece)));
        mRepository.insertRecipe(recipe);
        mRecipes = mRepository.getRecipes();
        // TODO: getting recipes doesn't work, use Food API for ingredients

    }

    public LiveData<List<Recipe>> getRecipes(int id) {
        if (mRecipes == null) {
            mRecipes = mRepository.getRecipes();
        }
        return mRecipes;
    }
}
