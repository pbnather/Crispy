package dk.au.itsmap.group4.crispy.service.foodapi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface FoodAPIService {

    @Headers("X-RapidAPI-Key: 12c76af865mshc278178c73ed2c7p182988jsn77a4571fa96a")
    @GET("food/ingredients/autocomplete")
    Call<List<IngredientApiName>> completeIngredients(@Query("query") String query);

    @Headers("X-RapidAPI-Key: 12c76af865mshc278178c73ed2c7p182988jsn77a4571fa96a")
    @GET("recipes/findByIngredients?ranking=1")
    Call<List<RecipeImage>> getImagesForRecipeByIngredients(@Query("ingredients") String ingredients, @Query("number") int number);

}