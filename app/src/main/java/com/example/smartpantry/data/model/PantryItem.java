package com.example.smartpantry.data.model;

/**
 * An ingredient the user has at home.
 * Plain Java class for now; Room annotations are added in the database step.
 */
public class PantryItem {

    private long id;
    private String name;
    private double quantity;
    private String unit;
    /** Expiry as UTC-midnight epoch millis, or null if the user did not enter one. */
    private Long expiryDate;

    public PantryItem(long id, String name, double quantity, String unit, Long expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Long expiryDate) {
        this.expiryDate = expiryDate;
    }
}
