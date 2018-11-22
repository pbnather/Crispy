package dk.au.itsmap.group4.crispy.database.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import dk.au.itsmap.group4.crispy.database.entity.Recipe;

@Dao
public interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Recipe... recipes);

    @Query("SELECT * FROM recipe")
    LiveData<List<Recipe>> getAll();

    @Query("SELECT * FROM recipe WHERE recipe.id = :id")
    LiveData<Recipe> getRecipeById(int id);

    @Delete
    void delete(Recipe recipe);
}
