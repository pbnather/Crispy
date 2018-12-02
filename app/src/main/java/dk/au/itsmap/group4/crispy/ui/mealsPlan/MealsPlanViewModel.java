package dk.au.itsmap.group4.crispy.ui.mealsPlan;

import java.util.Calendar;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import dk.au.itsmap.group4.crispy.database.FSRepository;
import dk.au.itsmap.group4.crispy.database.entity.Meal;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.model.IRepository;

public class MealsPlanViewModel extends ViewModel {

    private Calendar selectedDate = Calendar.getInstance();
    private String selectedRecipeName;

    private IRepository mRepository;
    private LiveData<List<IMeal>> mMeals;
    private LiveData<List<IRecipe>> mRecipes;

    public MealsPlanViewModel() {
        mRepository = FSRepository.getInstance();
    }

    public LiveData<List<IMeal>> getAllMeals() {
        if (mMeals == null) {
            mMeals = mRepository.getAllMeals();
        }
        return mRepository.getAllMeals();
    }

    public LiveData<IMeal> getMealById(String id) {
        return mRepository.getMealById(id);
    }


    public LiveData<IRecipe> getRecipeById(String id) {
        return mRepository.getRecipeById(id);
    }

    public LiveData<List<IRecipe>> getAllRecipes() {
        if (mRecipes == null) {
            mRecipes = mRepository.getAllRecipes();
        }
        return mRecipes;
    }

    public Calendar getSelectedDate() {
        return selectedDate;
    }

    public String getSelectedRecipe() {
        return selectedRecipeName;
    }

    public void setSelectedRecipe(String selectedRecipe) {
        this.selectedRecipeName = selectedRecipe;
    }

    public void createMeal() {
        Meal meal = new Meal(
                selectedRecipeName,
                "",
                "Vojta",
                null,
                selectedDate.getTime()
        );
        mRepository.saveMeal(meal);
    }
}
