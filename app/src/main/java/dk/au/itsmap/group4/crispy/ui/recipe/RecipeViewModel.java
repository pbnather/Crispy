package dk.au.itsmap.group4.crispy.ui.recipe;

import android.app.Application;
import android.util.Log;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.database.FSRepository;
import dk.au.itsmap.group4.crispy.database.entity.Recipe;
import dk.au.itsmap.group4.crispy.model.IIngredient;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.model.IRepository;
import dk.au.itsmap.group4.crispy.service.foodapi.FoodAPIService;
import dk.au.itsmap.group4.crispy.service.foodapi.RecipeImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ViewModel shared between RecipeList and RecipeDetail Activity
 */
public class RecipeViewModel extends AndroidViewModel {

    /**
     * Mode of Recipes views
     */
    public enum Mode {
        LIST, VIEW, EDIT, ADD
    }

    // if current layout has two columns
    private boolean mIsSinglePage = false;
    private MutableLiveData<Mode> mMode;

    private final IRepository mRepository;
    private LiveData<List<IRecipe>> mRecipes;
    private LiveData<IRecipe> mSelectedRecipe;
    private MutableLiveData<String> mSelectedRecipeId;

    private LiveData<List<IIngredient>> mSelectedRecipeIngredients;

    public RecipeViewModel(@NonNull Application application) {
        super(application);

        mRepository = FSRepository.getInstance();
        mSelectedRecipeId = new MutableLiveData<>();
        mSelectedRecipe = Transformations.switchMap(mSelectedRecipeId, recipeId -> {
            return recipeId != null ? mRepository.getRecipeById(recipeId) : new LiveData<IRecipe>(){};
        });

        mSelectedRecipeIngredients = Transformations.switchMap(mSelectedRecipeId, recipeId -> {
            return recipeId != null ? mRepository.getIngredientsForRecipeById(recipeId) : new LiveData<List<IIngredient>>() {};
        });

        mMode = new MutableLiveData<>();

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

    public LiveData<List<IIngredient>> getIngredientsForRecipeById(String id) {
        return mRepository.getIngredientsForRecipeById(id);
    }

    public LiveData<IRecipe> getSelectedRecipe() {
        return mSelectedRecipe;
    }

    public void selectRecipeById(String recipeId) {
        mSelectedRecipeId.setValue(recipeId);
    }
    public void selectRecipe(IRecipe recipe) {
        if(recipe != null) {
            selectRecipeById(recipe.getId());
        } else {
            selectRecipeById(null);
        }
    }

    public void saveRecipe(IRecipe recipe, List<IIngredient> added, List<IIngredient> deleted){
        if (recipe.getId() != null){
            mRepository.deleteIngredientsForRecipe(recipe, deleted);
        }
        String id = mRepository.saveRecipeWithIngredients(recipe, added);
        recipe.setId(id);
        addPhotoToRecipe(recipe, added);
        selectRecipe(recipe);
    }

    private void addPhotoToRecipe(IRecipe recipe, List<IIngredient> ingredients) {
        StringBuilder ingredientsQuery = new StringBuilder();
        for(IIngredient ingredient : ingredients) {
            ingredientsQuery.append(ingredient.getName());
            ingredientsQuery.append(",");
        }
        int end = ingredientsQuery.length() - 1;
        if(end > 0) {
            ingredientsQuery.deleteCharAt(end);
            String query = ingredientsQuery.toString();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            FoodAPIService foodApi = retrofit.create(FoodAPIService.class);

            Call<List<RecipeImage>> recipeImages = foodApi.getImagesForRecipeByIngredients(query, 1);
            recipeImages.enqueue(new Callback<List<RecipeImage>>() {
                @Override
                public void onResponse(Call<List<RecipeImage>> call, Response<List<RecipeImage>> response) {
                    if (response.body() != null && !response.body().isEmpty()) {
                        String image = response.body().get(0).getImage();
                        recipe.setImage_url(image);
                        mRepository.saveRecipe(recipe);
                    }
                }

                @Override
                public void onFailure(Call<List<RecipeImage>> call, Throwable t) {
                    Log.w("TAG", "Error fetching recipe image from API", t);
                }
            });
        }
    }

    public void deleteRecipe(Recipe recipe, List<IIngredient> ingredients) {
        if(ingredients != null) {
            mRepository.deleteIngredientsForRecipe(recipe, ingredients);
        }
        mRepository.deleteRecipe(recipe);
    }

    public String[] getPossibleUnits() {
        return new String[] {
                (String) getApplication().getText(R.string.kg),
                (String) getApplication().getText(R.string.g),
                (String) getApplication().getText(R.string.l),
                (String) getApplication().getText(R.string.ml),
                (String) getApplication().getText(R.string.pc)
        };
    }

    public int unitNr(String name){
        String[] units = getPossibleUnits();
        for(int i=0; i<units.length; i++)
        {
            if (units[i].contentEquals(name))
                return i;
        }
        return 0;
    }

    public boolean isSinglePage() {
        return mIsSinglePage;
    }

    public void setIsSinglePage(boolean isSinglePage) {
        this.mIsSinglePage = isSinglePage;
    }

    public LiveData<Mode> getMode() {
        return mMode;
    }

    public void setMode(Mode mode) {
        this.mMode.setValue(mode);
    }
}
