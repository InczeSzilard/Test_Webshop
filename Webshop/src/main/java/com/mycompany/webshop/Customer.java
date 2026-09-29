package com.mycompany.webshop;

public class Customer {

    private String id;
    private String firstName;
    private String lastName;
    private String loyaltyTier;

    public Customer(String id, String firstName,
            String lastName, String loyaltyTier) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.loyaltyTier = loyaltyTier;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLoyaltyTier() {
        return loyaltyTier;
    }

    public String toString() {
        return firstName + " " + lastName
                + " (" + loyaltyTier + ")";
    }
}