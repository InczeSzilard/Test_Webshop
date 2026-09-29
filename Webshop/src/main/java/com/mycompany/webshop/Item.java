package com.mycompany.webshop;

import com.mycompany.webshop.BaseEntity;
import com.mycompany.webshop.Discount;
import java.util.ArrayList;

public abstract class Item extends BaseEntity implements Billable {

    protected String sku;
    protected String name;
    protected int quantity;
    protected double price;

    protected ArrayList<Discount> discounts;

    public Item(String sku, String name, int quantity, double price) {
        super(sku);

        this.sku = sku;
        this.name = name;
        this.quantity = quantity;
        this.price = price;

        this.discounts = new ArrayList<Discount>();
    }

    public Item() {
        this("", "", 0, 0.0);
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ArrayList<Discount> getDiscounts() {
        return discounts;
    }

    public void addDiscount(Discount discount) {
        discounts.add(discount);
    }

    public double getDiscountTotal() {

        double total = 0.0;

        for (int i = 0; i < discounts.size(); i++) {
            total += discounts.get(i).getAmount();
        }

        return total;
    }

    /*
     * Overloaded method.
     */
    public double calculateTotal(double extraDiscountPercent) {

        double total = calculateTotal();

        return total - total * extraDiscountPercent;
    }

    public String businessKey() {
        return sku;
    }

    public String toString() {
        return name + " [" + sku + "] x" + quantity;
    }
}