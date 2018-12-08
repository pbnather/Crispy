package dk.au.itsmap.group4.crispy.ui.mealsPlan.mealsList;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.utils.GlideApp;
import dk.au.itsmap.group4.crispy.utils.TimeUtils;

/* Separation of OnClickListeners from Activity inspired by:
 * https://github.com/guenodz/livedata-recyclerview-sample/blob/master/app/src/main/java/me/guendouz/livedata_recyclerview/PostsAdapter.java
 */
public class MealsPlanRecyclerViewAdapter extends RecyclerView.Adapter<MealsPlanRecyclerViewAdapter.ViewHolder> {

    private final Context mContext;

    public interface OnRecyclerViewItemClickListener {
        void onItemClicked(IMeal meal);
    }

    private final OnRecyclerViewItemClickListener mClickListener;
    private List<IMeal> mValues;

    MealsPlanRecyclerViewAdapter(
            Context context,
            OnRecyclerViewItemClickListener clickListener
    ) {
        mContext = context;
        mClickListener = clickListener;
        mValues = new ArrayList<>();
    }

    public void setData(List<IMeal> newData) {
        this.mValues = newData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.bind(mValues.get(position), position - 1 >= 0 ? mValues.get(position - 1) : null);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meals_plan_meal_content, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mDay;
        final TextView mTitle;
        final TextView mHour;
        final TextView mUsersCooking;
        final ImageView mRecipeImage;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mDay = view.findViewById(R.id.day_date);
            mTitle =  view.findViewById(R.id.mealTitle);
            mHour = view.findViewById(R.id.mealHour);
            mUsersCooking = view.findViewById(R.id.usersCooking);
            mRecipeImage = view.findViewById(R.id.recipeImageMeal);
        }

        void bind(final IMeal item, final IMeal prevItem) {
            if (item != null) {
                SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, d. MMM", Locale.US);
                // show date only if is this meal on different date then previous
                if (!(prevItem != null && sdf.format(item.getDate()).equals(sdf.format(prevItem.getDate())))) {
                    String str;
                    if(item.getDate() == null) {
                        str = "---";
                    }
                    else if(TimeUtils.isToday(item.getDate())) {
                        str = "Today";
                    }
                    else if(TimeUtils.isTomorrow(item.getDate())) {
                        str = "Tomorrow";
                    } else {
                        str = sdf.format(item.getDate());
                    }

                    mDay.setText(str);
                    mDay.setVisibility(View.VISIBLE);
                } else {
                    mDay.setVisibility(View.GONE);
                }

                mHour.setText(DateUtils.formatDateTime(mContext, item.getDate().getTime(), DateUtils.FORMAT_SHOW_TIME));


                GlideApp.with(mView)
                        .load(item.getImage_url())
                        .placeholder(R.drawable.crispy_icon)
                        .centerCrop()
                        .into(mRecipeImage);

                mTitle.setText(item.getTitle());

                mUsersCooking.setText(item.getCookName());

                itemView.setTag(item);

                itemView.setOnClickListener(v -> {
                    if (mClickListener != null)
                        mClickListener.onItemClicked(item);
                });

            }
        }
    }
}