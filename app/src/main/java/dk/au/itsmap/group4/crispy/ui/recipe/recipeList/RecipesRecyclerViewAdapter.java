package dk.au.itsmap.group4.crispy.ui.recipe.recipeList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IRecipe;


// separating of OnClickListeners to Activity inspired by:
// https://github.com/guenodz/livedata-recyclerview-sample/blob/master/app/src/main/java/me/guendouz/livedata_recyclerview/PostsAdapter.java
public class RecipesRecyclerViewAdapter extends RecyclerView.Adapter<RecipesRecyclerViewAdapter.ViewHolder> {

    public interface OnRecipeClickListener {
        void onRecipeClicked(IRecipe recipe);
    }

    private final Context mContext;
    private final OnRecipeClickListener mOnRecipeClickListener;
    private List<IRecipe> mValues;


    RecipesRecyclerViewAdapter(
            Context parent,
            OnRecipeClickListener onRecipeClickListener
    ) {
        mContext = parent;
        mOnRecipeClickListener = onRecipeClickListener;
        mValues = new ArrayList<>();
    }

    public void setData(List<IRecipe> newData) {
        this.mValues = newData;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);

            mContentView = (TextView) view.findViewById(R.id.content);
        }

        void bind(final IRecipe recipe) {
            if (recipe != null) {

                mContentView.setText(recipe.getTitle());

                itemView.setTag(recipe);

                itemView.setOnClickListener(v -> {
                    if (mOnRecipeClickListener != null)
                        mOnRecipeClickListener.onRecipeClicked(recipe);
                });

            }
        }
    }
}