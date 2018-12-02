package dk.au.itsmap.group4.crispy.ui.mealsPlan.mealsList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.ui.GenericRecyclerViewAdapter;


public class MealsPlanRecyclerViewAdapter extends GenericRecyclerViewAdapter<IMeal> {

    private final OnRecyclerViewItemClickListener mClickListener;

    MealsPlanRecyclerViewAdapter(
        OnRecyclerViewItemClickListener clickListener
    ) {
        this.mClickListener = clickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meals_plan_meal_content, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends AbstractViewHolder {
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
        protected void bind(final IMeal item) {
            if (item != null) {
                mDay.setText(item.getDate() != null ? item.getDate().toString() : "---");
                mTitle.setText(item.getTitle());
                mHour.setText(String.format("%d:%d", item.getDateHours(), item.getDateMinutes()));
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