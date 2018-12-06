package dk.au.itsmap.group4.crispy.ui.recipe.recipeDetail;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import dk.au.itsmap.group4.crispy.service.foodapi.FoodAPIService;
import dk.au.itsmap.group4.crispy.service.foodapi.IngredientApiName;
import dk.au.itsmap.group4.crispy.service.foodapi.RecipeImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AutoCompleteIngredientAdapter extends ArrayAdapter<String> implements Filterable {
    private List<IngredientApiName> mIngredientNames;
    private List<String> mResults;
    private FoodAPIService mFoodApi;

    AutoCompleteIngredientAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        mIngredientNames = new ArrayList<>();
        mResults = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mFoodApi = retrofit.create(FoodAPIService.class);
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public String getItem(int index) {
        return mResults.get(index);
    }

    public void setData(List<IngredientApiName> newIngredientNames) {
        this.mIngredientNames = newIngredientNames;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null) {

                    String query = constraint.toString().toLowerCase();
                    Call<List<IngredientApiName>> results = mFoodApi.completeIngredients(query);
                    results.enqueue(new Callback<List<IngredientApiName>>() {
                        @Override
                        public void onResponse(Call<List<IngredientApiName>> call, Response<List<IngredientApiName>> response) {
                            List<String> list = new ArrayList<>();
                            for (IngredientApiName autoCompleteIngredient : response.body()) {
                                String name = autoCompleteIngredient.getName();
                                list.add(name);
                            }
                            mResults = list;
                            setData(response.body());
                        }

                        @Override
                        public void onFailure(Call<List<IngredientApiName>> call, Throwable t) {
                            Log.w("TAG", "Error fetching ingredient names from API", t);
                        }
                    });
                    filterResults.values = mResults;
                    filterResults.count = mResults.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}