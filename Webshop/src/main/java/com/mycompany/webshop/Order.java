package com.mycompany.webshop;

import com.mycompany.webshop.BaseEntity;
import java.util.ArrayList;

public class Order extends BaseEntity {

    private String status;
    private String currency;
    private Customer customer;

    private ArrayList<Item> items;

    private double taxRate;

    public Order(String id, String status,
            String currency, Customer customer,
            double taxRate) {

        super(id);

        this.status = status;
        this.currency = currency;
        this.customer = customer;
        this.taxRate = taxRate;

        this.items = new ArrayList<Item>();
    }

    public Order() {
        this("", "", "", null, 0.0);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public String businessKey() {
        return id;
    }

    /*
     * Calculate order total.
     */
    public double calculateTotal() {

        double subtotal = 0.0;

        for (int i = 0; i < items.size(); i++) {

            Item item = items.get(i);

            double lineTotal = item.calculateTotal();

            /*
             * GOLD gets an extra 5% discount,
             * but ONLY for physical goods.
             */
            if (customer != null
                    && "GOLD".equals(customer.getLoyaltyTier())
                    && item instanceof PhysicalItem) {

                lineTotal = lineTotal * 0.95;
            }

            subtotal += lineTotal;
        }

        double tax = subtotal * taxRate;

        return subtotal + tax;
    }

    /*
     * Overloaded method.
     */
    public double calculateTotal(boolean includeTax) {

        double subtotal = 0.0;

        for (int i = 0; i < items.size(); i++) {

            Item item = items.get(i);

            double lineTotal = item.calculateTotal();

            if (customer != null
                    && "GOLD".equals(customer.getLoyaltyTier())
                    && item instanceof PhysicalItem) {

                lineTotal = lineTotal * 0.95;
            }

            subtotal += lineTotal;
        }

        if (includeTax) {
            return subtotal + subtotal * taxRate;
        }

        return subtotal;
    }

    /*
     * Interface used directly in logic.
     */
    public double calculateBillableTotal() {

        double total = 0.0;

        for (int i = 0; i < items.size(); i++) {

            Billable billable = items.get(i);

            total += billable.calculateTotal();
        }

        return total;
    }

    /*
     * Physical items require shipping.
     */
    public boolean hasPhysicalItems() {

        for (int i = 0; i < items.size(); i++) {

            Item item = items.get(i);

            if (item instanceof PhysicalItem) {
                return true;
            }
        }

        return false;
    }

    public String toString() {

        return "Order " + id
                + " - " + status
                + " - " + customer;
    }
}