package com.mycompany.webshop;

public class DigitalItem extends Item {

    private String licenseKey;

    public DigitalItem(String sku, String name,
            int quantity, double price, String licenseKey) {

        super(sku, name, quantity, price);

        this.licenseKey = licenseKey;
    }

    public double calculateTotal() {

        return (price * quantity) - getDiscountTotal();
    }

    public String toString() {
        return "DIGITAL: " + super.toString();
    }
}
