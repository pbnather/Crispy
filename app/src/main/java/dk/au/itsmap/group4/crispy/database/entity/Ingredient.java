package dk.au.itsmap.group4.crispy.database.entity;

import com.google.firebase.firestore.Exclude;

import dk.au.itsmap.group4.crispy.model.IIngredient;

public class Ingredient extends Entity implements IIngredient {

    private String name;
    private String unit;
    private double quantity;

    public Ingredient() {}

    public Ingredient(String id, String name, String unit, double quantity) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
    }

    @Override
    @Exclude
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public double getQuantity() {
        return quantity;
    }
}
