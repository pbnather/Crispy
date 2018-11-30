package dk.au.itsmap.group4.crispy.viewmodel;

import android.app.Application;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import dk.au.itsmap.group4.crispy.database.FSRepository;
import dk.au.itsmap.group4.crispy.database.entity.Recipe;
import dk.au.itsmap.group4.crispy.model.Repository;

public class RecipeDetailViewModel extends AndroidViewModel {

    private Repository mRepository;
    private LiveData<List<Recipe>> mRecipes;

    public RecipeDetailViewModel(@NonNull Application application) {
        super(application);

        mRepository = FSRepository.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes() {
        if (mRecipes == null) {
            mRecipes = mRepository.getRecipes();
        }
        return mRecipes;
    }

}
