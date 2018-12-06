package dk.au.itsmap.group4.crispy.ui.mealsPlan.addPlannedMeal;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import dk.au.itsmap.group4.crispy.model.IRecipe;

public class AutoCompleteRecipeAdapter extends ArrayAdapter<IRecipe> implements Filterable {
    private List<IRecipe> mValues;
    private List<IRecipe> mResults;

    AutoCompleteRecipeAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        mValues = new ArrayList<>();
        mResults = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public IRecipe getItem(int index) {
        return mResults.get(index);
    }

    public void setData(List<IRecipe> newData) {
        this.mValues = newData;
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

                    // Now assign the values and count to the FilterResults object
                    List<IRecipe> list = new ArrayList<>();
                    for (IRecipe mValue : mValues) {
                        String title = mValue.getTitle();
                        if (title.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            list.add(mValue);
                        }
                    }
                    mResults = list;

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