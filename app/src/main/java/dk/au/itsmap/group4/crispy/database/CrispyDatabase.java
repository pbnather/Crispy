package dk.au.itsmap.group4.crispy.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import dk.au.itsmap.group4.crispy.database.dao.IngredientDao;
import dk.au.itsmap.group4.crispy.database.dao.ProductDao;
import dk.au.itsmap.group4.crispy.database.dao.RecipeDao;
import dk.au.itsmap.group4.crispy.database.entity.Ingredient;
import dk.au.itsmap.group4.crispy.database.entity.Product;
import dk.au.itsmap.group4.crispy.database.entity.Recipe;

@Database(entities = {Recipe.class, Ingredient.class, Product.class}, version = 1)
@TypeConverters({CrispyConverters.class})
public abstract class CrispyDatabase extends RoomDatabase {

    public abstract RecipeDao recipeDao();
    public abstract IngredientDao ingredientDao();
    public abstract ProductDao productDao();

    private static CrispyDatabase sInstance;
    public static CrispyDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (CrispyDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            CrispyDatabase.class,"crispy-db")
                            .build();
                }
            }
        }
        return sInstance;
    }

}
