package com.mycompany.webshop;

public class ServiceItem extends Item {

    private String provider;

    public ServiceItem(String sku, String name,
            int quantity, double price, String provider) {

        super(sku, name, quantity, price);

        this.provider = provider;
    }

    public double calculateTotal() {

        return (price * quantity) - getDiscountTotal();
    }

    public String toString() {
        return "SERVICE: " + super.toString();
    }
}