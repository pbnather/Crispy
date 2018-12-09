package dk.au.itsmap.group4.crispy.ui.mealsPlan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import dk.au.itsmap.group4.crispy.database.FSRepository;
import dk.au.itsmap.group4.crispy.database.entity.Meal;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.model.IRepository;

public class MealsPlanViewModel extends ViewModel {

    /**
     * Mode of AddPlannedMealFragment
     */
    public enum Mode {
        EDIT, ADD
    }

    // meal to be created
    private MutableLiveData<IMeal> mSelectedMeal;
    private Mode mMode;

    private IRepository mRepository;
    private LiveData<List<IMeal>> mMeals;
    private LiveData<List<IRecipe>> mRecipes;
    private LiveData<List<Day>> mDays;

    public MealsPlanViewModel() {
        mRepository = FSRepository.getInstance();

        mSelectedMeal = new MutableLiveData<>();
        mSelectedMeal.setValue(new Meal());
        mMode = Mode.ADD;

        mDays = Transformations.map(mRepository.getAllMeals(), meals -> {
            IMeal prevMeal = null;
            Day day = null;
            List<Day> days = new ArrayList<>();
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, d. MMM", Locale.US);
            for(IMeal meal : meals) {
                if(prevMeal == null) {
                    day = new Day(meal.getDate());
                    day.meals.add(meal);
                } else if (!(sdf.format(meal.getDate()).equals(sdf.format(prevMeal.getDate())))) {
                    days.add(day);
                    // first meal in a day
                    day = new Day(meal.getDate());
                    day.meals.add(meal);
                } else {
                    // next meal in a day
                    day.meals.add(meal);
                }
//                TODO: if(day.meals.size() == 4);
                prevMeal = meal;
            }
            days.add(day);
            return days;
        });
    }

    public LiveData<List<Day>> getDays() {
        return mDays;
    }

    public LiveData<List<IMeal>> getAllMeals() {
        if (mMeals == null) {
            mMeals = mRepository.getAllMeals();
        }
        return mRepository.getAllMeals();
    }

    public LiveData<List<IRecipe>> getAllRecipes() {
        if (mRecipes == null) {
            mRecipes = mRepository.getAllRecipes();
        }
        return mRecipes;
    }

    public MutableLiveData<IMeal> getSelectedMeal() {
        return mSelectedMeal;
    }

    public void selectMeal(IMeal meal) {
        mSelectedMeal.setValue(meal);
    }

    public void createMeal() {
        mRepository.saveMeal(mSelectedMeal.getValue());
        mSelectedMeal.setValue(new Meal());
    }

    public void deleteSelectedMeal() {
        mRepository.deleteMeal(mSelectedMeal.getValue());
    }

    public Mode getMode() {
        return mMode;
    }

    /**
     * Prepare View model for editing
     * @param toEdit IMeal
     */
    public void switchToEditMode(IMeal toEdit) {
        mMode = Mode.EDIT;
        mSelectedMeal.setValue(toEdit);
    }

    /**
     * Prepare View model for creating new Meals
     */
    public void switchToAddMode() {
        mMode = Mode.ADD;
        mSelectedMeal.setValue(new Meal());
    }

    public class Day {
        public Date date;
        public List<IMeal> meals;

        public Day(Date date) {
            this.date = date;
            meals = new ArrayList<>();
        }
    }
}