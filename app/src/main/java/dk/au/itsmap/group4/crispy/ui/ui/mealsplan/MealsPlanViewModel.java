package dk.au.itsmap.group4.crispy.ui.ui.mealsplan;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import dk.au.itsmap.group4.crispy.database.FSRepository;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.model.IRepository;

public class MealsPlanViewModel extends ViewModel {

    private IRepository mRepository;
    private LiveData<List<IMeal>> mMeals;

    public MealsPlanViewModel() {
        mRepository = FSRepository.getInstance();

    }

    public LiveData<List<IMeal>> getAllMeals() {
        if (mMeals == null) {
            mMeals = mRepository.getAllMeals();
        }
        return mMeals;
    }

    public LiveData<IMeal> getMealById(String id) {
        return mRepository.getMealById(id);
    }


}
