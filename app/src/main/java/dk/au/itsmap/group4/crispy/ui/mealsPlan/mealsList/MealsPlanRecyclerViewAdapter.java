package dk.au.itsmap.group4.crispy.ui.mealsPlan.mealsList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.service.GlideApp;
import dk.au.itsmap.group4.crispy.utils.TimeUtils;

// separating of OnClickListeners to Activity inspired by:
// https://github.com/guenodz/livedata-recyclerview-sample/blob/master/app/src/main/java/me/guendouz/livedata_recyclerview/PostsAdapter.java
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(mValues.get(position), position - 1 >= 0 ? mValues.get(position - 1) : null);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
            mDay = (TextView) view.findViewById(R.id.day_date);
            mTitle = (TextView) view.findViewById(R.id.mealTitle);
            mHour = (TextView) view.findViewById(R.id.mealHour);
            mUsersCooking = (TextView) view.findViewById(R.id.usersCooking);
            mRecipeImage = view.findViewById(R.id.recipeImageMeal);
        }

        protected void bind(final IMeal item, final IMeal prevItem) {
            if (item != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d. MMM");
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