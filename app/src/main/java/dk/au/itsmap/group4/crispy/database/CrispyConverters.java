package dk.au.itsmap.group4.crispy.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import androidx.room.TypeConverter;
import dk.au.itsmap.group4.crispy.database.entity.Ingredient;
import dk.au.itsmap.group4.crispy.database.entity.Product;
import dk.au.itsmap.group4.crispy.database.entity.Unit;

public class CrispyConverters {
    @TypeConverter
    public static String ListToString(List<Ingredient> value) {
        return value == null ? null : new Gson().toJson(value);
    }

    @TypeConverter
    public static List<Ingredient> fromList(String value) {
        return value == null ? null : new Gson().fromJson(value, new TypeToken<List<Ingredient>>() {}.getType());
    }

    @TypeConverter
    public static String ProductToString(Product value) {
        return value == null ? null : new Gson().toJson(value);
    }

    @TypeConverter
    public static Product fromProduct(String value) {
        return value == null ? null : new Gson().fromJson(value, new TypeToken<Product>() {}.getType());
    }

    @TypeConverter
    public static String UnitToString(Unit value) {
        return value == null ? null : new Gson().toJson(value);
    }

    @TypeConverter
    public static Unit fromUnit(String value) {
        return value == null ? null : new Gson().fromJson(value, new TypeToken<Unit>() {}.getType());
    }
}
