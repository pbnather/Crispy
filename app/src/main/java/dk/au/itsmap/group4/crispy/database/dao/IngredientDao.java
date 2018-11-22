package dk.au.itsmap.group4.crispy.database.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import dk.au.itsmap.group4.crispy.database.entity.Ingredient;

// TODO: Add BaseDao interface and inherit all the methods
@Dao
public interface IngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Ingredient... ingredients);

    @Query("SELECT * FROM ingredient")
    LiveData<List<Ingredient>> getAll();

    @Query("SELECT * FROM ingredient WHERE ingredient.id = :id")
    LiveData<Ingredient> getIngredientById(int id);

    @Delete
    void delete(Ingredient ingredient);
}
