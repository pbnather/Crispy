package dk.au.itsmap.group4.crispy.ui.mealsPlan.addPlannedMeal;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dk.au.itsmap.group4.crispy.model.IRecipe;

public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private List<IRecipe> mValues;
    private List<String> mResults;
    private Context mContext;
    private AutoCompleteTextView mTextView;

    public AutoCompleteAdapter(Context context, int textViewResourceId, AutoCompleteTextView textView) {
        super(context, textViewResourceId);
        mValues = new ArrayList<>();
        mResults = new ArrayList<>();
        mContext = context;
        mTextView = textView;
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public String getItem(int index) {
        return mResults.get(index);
    }

    public void setData(List<IRecipe> newData) {
        this.mValues = newData;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null) {

                    // Now assign the values and count to the FilterResults object
                    mResults = mValues.stream()
                            .map(IRecipe::getTitle)
                            .filter(title -> title.toLowerCase().contains(constraint.toString().toLowerCase()))
                            .collect(Collectors.toList());

                    filterResults.values = mResults;
                    filterResults.count = mResults.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if(results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }
}