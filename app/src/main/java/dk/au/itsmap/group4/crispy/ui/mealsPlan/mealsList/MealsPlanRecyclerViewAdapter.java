package dk.au.itsmap.group4.crispy.ui.mealsPlan.mealsList;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.ui.mealsPlan.MealsPlanViewModel;
import dk.au.itsmap.group4.crispy.utils.GlideApp;
import dk.au.itsmap.group4.crispy.utils.TimeUtils;

/* Separation of OnClickListeners from Activity inspired by:
 * https://github.com/guenodz/livedata-recyclerview-sample/blob/master/app/src/main/java/me/guendouz/livedata_recyclerview/PostsAdapter.java
 */
public class MealsPlanRecyclerViewAdapter extends RecyclerView.Adapter<MealsPlanRecyclerViewAdapter.ViewHolder> {

    private final Context mContext;

    public interface OnRecyclerViewItemClickListener {
        void onItemClicked(IMeal meal);
        void onEditButtonClicked(IMeal meal);
    }

    private final OnRecyclerViewItemClickListener mClickListener;
    private List<MealsPlanViewModel.Day> mValues;

    MealsPlanRecyclerViewAdapter(
            Context context,
            OnRecyclerViewItemClickListener clickListener
    ) {
        mContext = context;
        mClickListener = clickListener;
        mValues = new ArrayList<>();
    }

    public void setData(List<MealsPlanViewModel.Day> newData) {
        this.mValues = newData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.bind(mValues.get(position));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meals_plan_day_item, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mDay;
        final List<View> mMeals;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mDay = view.findViewById(R.id.meal_title);
            mMeals = new ArrayList<>();
            mMeals.add(view.findViewById(R.id.meal_1));
            mMeals.add(view.findViewById(R.id.meal_2));
            mMeals.add(view.findViewById(R.id.meal_3));
            mMeals.add(view.findViewById(R.id.meal_4));
        }

        void bind(final MealsPlanViewModel.Day item) {
            if (item != null) {
                SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, d. MMM", Locale.US);
                    String str;
                    if(item.date == null) {
                        str = "---";
                    }
                    else if(TimeUtils.isToday(item.date)) {
                        str = "Today";
                    }
                    else if(TimeUtils.isTomorrow(item.date)) {
                        str = "Tomorrow";
                    } else {
                        str = sdf.format(item.date);
                    }

                    mDay.setText(str);

                    for(int i = 0; i < 4-item.meals.size(); i++) {
                        mMeals.get(3-i).setVisibility(View.GONE);
                    }
                    int i = 0;
                    for(IMeal meal : item.meals) {
                        mMeals.get(i).setVisibility(View.VISIBLE);
                        TextView mealTitle = mMeals.get(i).findViewById(R.id.meal_title);
                        TextView mealTime = mMeals.get(i).findViewById(R.id.mealHour);
                        TextView mealCook = mMeals.get(i).findViewById(R.id.usersCooking);
                        ImageView userImage = mMeals.get(i).findViewById(R.id.userImage);
                        ImageView mealImage = mMeals.get(i).findViewById(R.id.recipeImageMeal);
                        Button btnEdit = mMeals.get(i).findViewById(R.id.editMealBtn);

                        mealTime.setText(DateUtils.formatDateTime(mContext, meal.getDate().getTime(), DateUtils.FORMAT_SHOW_TIME));

                        GlideApp.with(mView)
                                .load(meal.getImage_url())
                                .placeholder(R.drawable.crispy_icon)
                                .centerCrop()
                                .transform(new RoundedCorners(20))
                                .into(mealImage);

                        mealTitle.setText(meal.getTitle());

                        mealCook.setText(meal.getCookName());

                        GlideApp.with(mView)
                                .load(meal.getCookImage())
                                .placeholder(R.drawable.crispy_icon)
                                .circleCrop()
                                .into(userImage);

                        mMeals.get(i).setOnClickListener(v -> {
                            if (mClickListener != null)
                                mClickListener.onItemClicked(meal);
                        });

                        mMeals.get(i).findViewById(R.id.editMealBtn).setOnClickListener(v-> {
                            if (mClickListener != null)
                                mClickListener.onEditButtonClicked(meal);
                        });

                        i++;

                }

            }
        }
    }
}