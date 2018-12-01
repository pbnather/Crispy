package dk.au.itsmap.group4.crispy.database.entity;

import dk.au.itsmap.group4.crispy.model.IIngredient;

public class Ingredient extends Entity implements IIngredient {

    private String name;
    private String unit;
    private double quantity;

    public Ingredient() {}

    @Override
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
