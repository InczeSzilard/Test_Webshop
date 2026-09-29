
package com.mycompany.webshop;

public class PhysicalItem extends Item {

    private double weight;

    public PhysicalItem(String sku, String name,
            int quantity, double price, double weight) {

        super(sku, name, quantity, price);

        this.weight = weight;
    }

    /*
     * Overriding calculateTotal().
     */
    public double calculateTotal() {

        return (price * quantity) - getDiscountTotal();
    }

    public double getWeight() {
        return weight;
    }

    public String toString() {
        return "PHYSICAL: " + super.toString();
    }
}