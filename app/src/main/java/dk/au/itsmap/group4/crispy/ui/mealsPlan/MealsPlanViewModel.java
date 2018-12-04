package dk.au.itsmap.group4.crispy.ui.mealsPlan;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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

    public MealsPlanViewModel() {
        mRepository = FSRepository.getInstance();

        mSelectedMeal = new MutableLiveData<>();
        mSelectedMeal.setValue(new Meal());
        mMode = Mode.ADD;

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

    public MutableLiveData<IMeal> getSelectedMeal() {
        return mSelectedMeal;
    }

    public void createMeal() {
        mRepository.saveMeal(mSelectedMeal.getValue());
        mSelectedMeal.setValue(new Meal());
    }

    public String[] getPossibleCooks() {
        return new String[] {
                "Vojta",
                "Pawel",
                "Ala"
        };
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
}