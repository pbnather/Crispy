package dk.au.itsmap.group4.crispy.ui.mealsPlan.mealsList;

import android.annotation.SuppressLint;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IMeal;

// separating of OnClickListeners to Activity inspired by:
// https://github.com/guenodz/livedata-recyclerview-sample/blob/master/app/src/main/java/me/guendouz/livedata_recyclerview/PostsAdapter.java
public class MealsPlanRecyclerViewAdapter extends RecyclerView.Adapter<MealsPlanRecyclerViewAdapter.ViewHolder> {

    public interface OnRecyclerViewItemClickListener {
        void onItemClicked(IMeal meal);
    }

    private final OnRecyclerViewItemClickListener mClickListener;
    private List<IMeal> mValues;

    MealsPlanRecyclerViewAdapter(
            OnRecyclerViewItemClickListener clickListener
    ) {
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
        final TextView mDay;
        final TextView mTitle;
        final TextView mHour;
        final TextView mUsersCooking;

        ViewHolder(View view) {
            super(view);
            mDay = (TextView) view.findViewById(R.id.day_date);
            mTitle = (TextView) view.findViewById(R.id.mealTitle);
            mHour = (TextView) view.findViewById(R.id.mealHour);
            mUsersCooking = (TextView) view.findViewById(R.id.usersCooking);
        }

        @SuppressLint("DefaultLocale")
        protected void bind(final IMeal item, final IMeal prevItem) {
            if (item != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d. MMM");
                // show date only if is this meal on different date then previous
                if (!(prevItem != null && sdf.format(item.getDate()).equals(sdf.format(prevItem.getDate())))) {
                    mDay.setText(item.getDate() != null ? sdf.format(item.getDate()) : "---");
                } else {
                    mDay.setVisibility(View.GONE);
                }

                mTitle.setText(item.getTitle());
                mHour.setText(String.format(DateFormat.getTimeInstance(DateFormat.SHORT).format(item.getDate())));

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