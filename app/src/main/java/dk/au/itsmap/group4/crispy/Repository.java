package dk.au.itsmap.group4.crispy;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import dk.au.itsmap.group4.crispy.database.CrispyDatabase;
import dk.au.itsmap.group4.crispy.database.dao.IngredientDao;
import dk.au.itsmap.group4.crispy.database.dao.ProductDao;
import dk.au.itsmap.group4.crispy.database.dao.RecipeDao;
import dk.au.itsmap.group4.crispy.database.entity.Recipe;

public class Repository {

    private RecipeDao mRecipeDao;
    private ProductDao mProductDao;
    private IngredientDao mIngredientDao;

    // adapted from BasicSample demo
    private static Repository sInstance;
    public static Repository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (Repository.class) {
                if (sInstance == null) {
                    sInstance = new Repository(context);
                }
            }
        }
        return sInstance;
    }

    private Repository(Context context) {
        CrispyDatabase db = CrispyDatabase.getInstance(context);
        mRecipeDao = db.recipeDao();
        mProductDao = db.productDao();
        mIngredientDao = db.ingredientDao();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeDao.getAll();
    }

    public LiveData<Recipe> getRecipe(int id) {
        return mRecipeDao.getRecipeById(id);
    }

    public void insertRecipe (Recipe recipe) {
        new insertAsyncTask(mRecipeDao).execute(recipe);
    }

    // adapted from BasicSample demo
    private static class insertAsyncTask extends AsyncTask<Recipe, Void, Void> {

        private RecipeDao mAsyncTaskDao;

        insertAsyncTask(RecipeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Recipe... params) {
            mAsyncTaskDao.insertAll(params[0]);
            return null;
        }
    }
}
