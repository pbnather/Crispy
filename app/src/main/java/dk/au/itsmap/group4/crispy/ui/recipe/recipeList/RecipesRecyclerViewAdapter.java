package dk.au.itsmap.group4.crispy.ui.recipe.recipeList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.utils.GlideApp;


/* Separation of OnClickListeners from Activity inspired by:
 * https://github.com/guenodz/livedata-recyclerview-sample/blob/master/app/src/main/java/me/guendouz/livedata_recyclerview/PostsAdapter.java
 */
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
        final ImageView mImageView;

        ViewHolder(View view) {
            super(view);

            mContentView = view.findViewById(R.id.content);
            mImageView = view.findViewById(R.id.recipeImage);
        }

        void bind(final IRecipe recipe) {
            if (recipe != null) {

                mContentView.setText(recipe.getTitle());
                GlideApp.with(mContext)
                        .load(recipe.getImage_url())
                        .placeholder(R.drawable.crispy_icon)
                        .centerCrop()
                        .into(mImageView);

                itemView.setTag(recipe);

                itemView.setOnClickListener(v -> {
                    if (mOnRecipeClickListener != null)
                        mOnRecipeClickListener.onRecipeClicked(recipe);
                });

            }
        }
    }
}