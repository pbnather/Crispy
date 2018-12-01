package dk.au.itsmap.group4.crispy.ui.ui.mealsplan;

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

        ViewHolder(View view) {
            super(view);
            mDay = (TextView) view.findViewById(R.id.day_date);
        }

        protected void bind(final IMeal item) {
            if (item != null) {
                mDay.setText("test");

                itemView.setTag(item);

                itemView.setOnClickListener(v -> {
                    if (mClickListener != null)
                        mClickListener.onItemClicked(item);
                });

            }
        }
    }
}