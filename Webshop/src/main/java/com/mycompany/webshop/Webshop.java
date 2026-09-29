package com.mycompany.webshop;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Webshop {

    public static void main(String[] args) {

        try {

            ArrayList<Order> orders =
                    OrderParser.parse("web.json");

            PrintWriter output =
                    new PrintWriter(
                            new FileWriter("report.txt"));

            output.println("===== ORDER REPORT =====");
            output.println();

            for (int i = 0; i < orders.size(); i++) {

                Order order = orders.get(i);

                output.println(order);

                output.println(
                        "Shipping required: "
                        + order.hasPhysicalItems());

                output.println(
                        "Total before tax: "
                        + order.calculateTotal(false));

                output.println(
                        "Total with tax: "
                        + order.calculateTotal(true));

                output.println();

                /*
                 * Sort items by line total.
                 */
                ArrayList<Item> sortedItems =
                        new ArrayList<Item>(
                                order.getItems());

                Collections.sort(
                        sortedItems,
                        new Comparator<Item>() {

                    public int compare(
                            Item a, Item b) {

                        double x =
                                a.calculateTotal();

                        double y =
                                b.calculateTotal();

                        if (x < y) {
                            return -1;
                        }

                        if (x > y) {
                            return 1;
                        }

                        return 0;
                    }
                });

                output.println("Items:");

                for (int j = 0;
                        j < sortedItems.size();
                        j++) {

                    Item item = sortedItems.get(j);

                    output.println(
                            "  " + item);

                    output.println(
                            "    Quantity: "
                            + item.getQuantity());

                    output.println(
                            "    Unit price: "
                            + item.getPrice());

                    output.println(
                            "    Discounts: "
                            + item.getDiscountTotal());

                    output.println(
                            "    Line total: "
                            + item.calculateTotal());
                }

                output.println();
                output.println("-------------------------");
                output.println();
            }

            output.println(
                    "Entities created: "
                    + BaseEntity.getEntityCount());

            output.close();

            /*
             * Concise console summary.
             */
            System.out.println(
                    "Orders loaded: "
                    + orders.size());

            for (int i = 0; i < orders.size(); i++) {

                Order order = orders.get(i);

                System.out.println(
                        order.getId()
                        + " total = "
                        + order.calculateTotal(true)
                        + " shipping = "
                        + order.hasPhysicalItems());
            }

            System.out.println(
                    "report.txt created.");

            System.out.println(
                    "Entities created: "
                    + BaseEntity.getEntityCount());

        } catch (Exception e) {

            System.out.println(
                    "Application error: "
                    + e.getMessage());

            e.printStackTrace();
        }
    }
}