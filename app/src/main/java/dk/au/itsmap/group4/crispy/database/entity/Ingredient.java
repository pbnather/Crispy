package dk.au.itsmap.group4.crispy.database.entity;

public class Ingredient {

    private String name;
    private String unit; // TODO: Try to map to enum
    private double quantity;

    public Ingredient() {}

    public Ingredient(String name, String unit, double quantity) {
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
    }
}
