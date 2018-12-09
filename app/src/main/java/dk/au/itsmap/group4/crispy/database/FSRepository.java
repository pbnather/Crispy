package dk.au.itsmap.group4.crispy.database;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import dk.au.itsmap.group4.crispy.database.entity.Ingredient;
import dk.au.itsmap.group4.crispy.database.entity.Meal;
import dk.au.itsmap.group4.crispy.database.entity.Recipe;
import dk.au.itsmap.group4.crispy.database.entity.UserGroup;
import dk.au.itsmap.group4.crispy.model.IIngredient;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.model.IRepository;
import dk.au.itsmap.group4.crispy.model.IUserGroup;

public class FSRepository implements IRepository {

    private final static String DEFAULT_GROUP_ID = "Ri6ynXhA0n3Da7MDOXe6";
    private final static String TAG = "FSRepository";

    private String groupId;

    private FirebaseFirestore mFirestore;
    private CollectionReference mRecipes;
    private CollectionReference mMeals;
    private CollectionReference mUsers;
    private CollectionReference mGroups;

    /* adapted from BasicSample demo */
    private static FSRepository sInstance;
    public static FSRepository getInstance() {
        if (sInstance == null) {
            synchronized (FSRepository.class) {
                if (sInstance == null) {
                    sInstance = new FSRepository();
                }
            }
        }
        return sInstance;
    }

    private FSRepository() {
        // Set up Firestore to use a new Timestamp.class
        mFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);

        // Get references to all relevant collections
        mRecipes = mFirestore.collection("recipes");
        mMeals = mFirestore.collection("meals");
        mUsers = mFirestore.collection("users");
        mGroups = mFirestore.collection("groups");
    }

    public LiveData<List<IUserGroup>> getUserGroup(@NonNull String userId) {
        Query groupQuery = mGroups.whereArrayContains("userIds", userId).limit(1);
        return new FSCollectionLiveData<>(groupQuery, UserGroup.class);
    }

    public void createUserWithGroup(@NonNull String userId, @NonNull String userName, @NonNull String photoUrl) {
        DocumentReference userRef = mUsers.document(userId);
        DocumentReference userGroupRef = mGroups.document();

        // Create new user document
        Map<String, Object> user = new HashMap<>();
        user.put("group", userGroupRef);

        // Add userId to groups userIds array
        List<String> userIds = new ArrayList<>();
        userIds.add(userId);

        // Create user info map
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", userName);
        userInfo.put("photo_url", photoUrl);

        // Add username to groups users map
        Map<String, Object> users = new HashMap<>();
        users.put(userId, userInfo);

        // Create new group documents
        Map<String, Object> group = new HashMap<>();
        group.put("name", "Your UserGroup");
        group.put("owner", userId);
        group.put("userIds", userIds);
        group.put("users", users);

        WriteBatch batch = mFirestore.batch();
        batch.set(userRef, user);
        batch.set(userGroupRef, group);
        batch.commit();
    }

    public void createUserAndAssignGroup(@NonNull String userId, @NonNull String groupId,
                                         @NonNull String userName, @NonNull String photoUrl) {
        DocumentReference userRef = mUsers.document(userId);
        DocumentReference userGroupRef = mGroups.document(groupId);

        // Create new user document
        Map<String, Object> user = new HashMap<>();
        user.put("group", userGroupRef);

        // Create user info map
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", userName);
        userInfo.put("photo_url", photoUrl);

        WriteBatch batch = mFirestore.batch();
        batch.set(userRef, user);
        batch.update(userGroupRef,"userIds", FieldValue.arrayUnion(userId));
        batch.update(userGroupRef,"users." + userId, userInfo);
        batch.commit();
    }

    public void createUserAndAssignDefaultGroup(@NonNull String userId, @NonNull String userName, @NonNull String photoUrl) {
        createUserAndAssignGroup(userId, DEFAULT_GROUP_ID, userName, photoUrl);
    }

    public void addUserToGroup(@NonNull String userId, @NonNull String groupId) {

    }

    public void deleteUserFromGroup(@NonNull String userId) {

    }

    @Override
    public LiveData<IMeal> getMealById(@NonNull String mealId) {
        DocumentReference docRef = mMeals.document(mealId);
        return new FSDocumentLiveData<>(docRef, Meal.class);
    }

    @Override
    public LiveData<IRecipe> getRecipeById(@NonNull String recipeId) {
        DocumentReference docRef = mRecipes.document(recipeId);
        return new FSDocumentLiveData<>(docRef, Recipe.class);
    }

    @Override
    public void getRecipeByIdOnce(@NonNull String recipeId, OnCompleteListener<DocumentSnapshot> listener) {
        DocumentReference docRef = mRecipes.document(recipeId);
        docRef.get().addOnCompleteListener(listener);
    }

    @Override
    public LiveData<IRecipe> getRecipeForMeal(@NonNull IMeal meal) {
        return null;
    }

    @Override
    public LiveData<List<IMeal>> getAllMeals() {
        return new FSCollectionLiveData<>(mMeals.whereGreaterThan("date", Timestamp.now()), Meal.class);
    }

    @Override
    public void getMealsInRangeOnce(Date from, Date to, OnCompleteListener<QuerySnapshot> listener) {
        Query query = mMeals.whereGreaterThan("date", new Timestamp(from)).whereLessThan("date", new Timestamp(to));
        query.get().addOnCompleteListener(listener);
    }

    @Override
    public LiveData<List<IRecipe>> getAllRecipes() {
        return new FSCollectionLiveData<>(mRecipes, Recipe.class);
    }

    @Override
    public LiveData<List<IIngredient>> getIngredientsForRecipe(@NonNull IRecipe recipe) {
        return getIngredientsForRecipeById(recipe.getId());
    }

    @Override
    public LiveData<List<IIngredient>> getIngredientsForRecipeById(@NonNull String recipeId) {
        CollectionReference ingredients = mRecipes.document(recipeId).collection("ingredients");
        return new FSCollectionLiveData<>(ingredients, Ingredient.class);
    }

    @Override
    public String saveMeal(@NonNull IMeal meal) {
        DocumentReference mealRef = meal.getId() == null ? mMeals.document() : mMeals.document(meal.getId());
        mealRef.set(meal)
                .addOnSuccessListener(v -> Log.d(TAG, "Saved meal " + mealRef.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error saving meal", e));
        return mealRef.getId();
    }

    @Override
    public String saveRecipe(@NonNull IRecipe recipe) {
        DocumentReference recipeRef = recipe.getId() == null ? mRecipes.document() : mRecipes.document(recipe.getId());
        recipeRef.set(recipe)
                .addOnSuccessListener(v -> Log.d(TAG, "Saved recipe " + recipeRef.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error saving recipe", e));
        updateMealsForRecipe(recipe);
        return recipeRef.getId();
    }

    private void updateMealsForRecipe(@NonNull IRecipe recipe) {
        if(recipe.getId() != null) {
            mMeals.whereEqualTo("recipeId", recipe.getId()).get()
                    .addOnSuccessListener(v -> mFirestore.runTransaction(new Transaction.Function<Void>() {
                        @Override
                        public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                            for(DocumentSnapshot doc : v.getDocuments()) {
                                DocumentReference mealRef = mMeals.document(doc.getId());
                                transaction.update(mealRef, "image_url", recipe.getImage_url());
                                transaction.update(mealRef, "title", recipe.getTitle());
                            }
                            return null;
                        }
                    }))
                    .addOnFailureListener(e -> Log.w(TAG, "Error updating meals for recipe", e));
        }
    }

    @Override
    public String saveIngredientsForRecipe(@NonNull IRecipe recipe, List<IIngredient> ingredients) {
        return saveIngredientsForRecipeById(recipe.getId(), ingredients);
    }

    @Override
    public String saveIngredientsForRecipeById(@NonNull String recipeId, List<IIngredient> ingredients) {
        // Initialize batch
        WriteBatch batch = mFirestore.batch();
        // Get recipe reference
        DocumentReference recipeRef = mRecipes.document(recipeId);
        CollectionReference ingredientsRef = recipeRef.collection("ingredients");
        for(IIngredient ingredient : ingredients) {
            // Get ingredient reference and add it to the batch
            String id = ingredient.getId();
            DocumentReference ingredientRef = id == null ?
                    ingredientsRef.document() :
                    ingredientsRef.document(id);
            batch.set(ingredientRef, ingredient);
        }
        // Save the ingredients in one batch
        batch.commit()
                .addOnSuccessListener(v -> Log.d(TAG, "Saved ingredients for recipe " + recipeRef.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error saving ingredients", e));
        return recipeRef.getId();
    }

    @Override
    public String saveRecipeWithIngredients(@NonNull IRecipe recipe, List<IIngredient> ingredients) {
        // TODO: Test if ingredients can be null, if so change saveRecipe()
        // Initialize batch
        WriteBatch batch = mFirestore.batch();
        // Get recipe reference and add it to the batch
        DocumentReference recipeRef = recipe.getId() == null ? mRecipes.document() : mRecipes.document(recipe.getId());
        CollectionReference ingredientsRef = recipeRef.collection("ingredients");
        batch.set(recipeRef, recipe);
        for(IIngredient ingredient : ingredients) {
            // Get ingredient reference and add it to the batch
            String id = ingredient.getId();
            DocumentReference ingredientRef = id == null ?
                    ingredientsRef.document() :
                    ingredientsRef.document(id);
            batch.set(ingredientRef, ingredient);
        }
        // Save the recipe with ingredients in one batch
        batch.commit()
                .addOnSuccessListener(v -> Log.d(TAG, "Saved recipe with ingredients " + recipeRef.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error saving recipe with ingredients", e));
        updateMealsForRecipe(recipe);
        return recipeRef.getId();
    }

    @Override
    public void deleteRecipe(@NonNull IRecipe recipe) {
        deleteRecipe(recipe.getId());
    }

    @Override
    public void deleteRecipe(@NonNull String recipeId) {
        // TODO: Delete also all the documents in the `ingredients` subcollection
        DocumentReference recipeRef = mRecipes.document(recipeId);
        recipeRef
                .delete()
                .addOnSuccessListener(v -> Log.d(TAG, "Deleted recipe " + recipeId))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting recipe", e));
    }

    @Override
    public void deleteMeal(@NonNull IMeal meal) {
        deleteMeal(meal.getId());
    }

    @Override
    public void deleteMeal(@NonNull String mealId) {
        DocumentReference recipeRef = mMeals.document(mealId);
        recipeRef
                .delete()
                .addOnSuccessListener(v -> Log.d(TAG, "Deleted meal " + mealId))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting meal", e));
    }

    @Override
    public void deleteIngredientsForRecipe(@NonNull IRecipe recipe, List<IIngredient> ingredients) {
        deleteIngredientsForRecipeById(recipe.getId(), ingredients);
    }

    @Override
    public void deleteIngredientsForRecipeById(@NonNull String recipeId, List<IIngredient> ingredients) {
        // Initialize batch
        WriteBatch batch = mFirestore.batch();
        // Get ingredients collection reference
        CollectionReference ingredientsRef = mRecipes.document(recipeId).collection("ingredients");
        for(IIngredient ingredient : ingredients) {
            // Get ingredient reference and add it to the batch
            String id = ingredient.getId();
            if(id != null) {
                DocumentReference ingredientRef = ingredientsRef.document(id);
                batch.delete(ingredientRef);
            }
        }
        // Delete the ingredients in one batch
        batch.commit()
                .addOnSuccessListener(v -> Log.d(TAG, "Deleted ingredients for recipe " + recipeId))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting ingredients", e));
    }

    public void deleteUser(@NonNull String userId, IUserGroup group) {
        if(group != null) {
            DocumentReference groupRef = mGroups.document(group.getId());
            group.deleteUser(userId);
            groupRef.set(group);
        }
    }
}