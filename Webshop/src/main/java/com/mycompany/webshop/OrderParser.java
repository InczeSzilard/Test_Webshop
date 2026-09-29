package com.mycompany.webshop;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.util.ArrayList;

public class OrderParser {

    public static ArrayList<Order> parse(String fileName)
            throws Exception {

        ArrayList<Order> orders = new ArrayList<Order>();

        FileReader reader = null;

        try {

            reader = new FileReader(fileName);

            JsonElement rootElement =
                    new JsonParser().parse(reader);

            JsonObject root =
                    rootElement.getAsJsonObject();

            JsonArray jsonOrders =
                    root.getAsJsonArray("orders");

            for (int i = 0; i < jsonOrders.size(); i++) {

                JsonObject jsonOrder =
                        jsonOrders.get(i).getAsJsonObject();

                try {

                    Order order = parseOrder(jsonOrder);

                    orders.add(order);

                } catch (DomainValidationException e) {

                    System.out.println(
                            "Skipping invalid order: "
                            + e.getMessage());
                }
            }

        } finally {

            if (reader != null) {
                reader.close();
            }
        }

        return orders;
    }

    private static Order parseOrder(JsonObject o)
            throws DomainValidationException {

        String id = requiredString(o, "orderId");

        String status = requiredString(o, "status");

        String currency = requiredString(o, "currency");

        JsonObject customerObject =
                requiredObject(o, "customer");

        Customer customer =
                parseCustomer(customerObject);

        JsonArray itemsArray =
                requiredArray(o, "items");

        double taxRate = 0.0;

        JsonArray taxes = o.getAsJsonArray("taxes");

        if (taxes != null && taxes.size() > 0) {

            JsonObject tax =
                    taxes.get(0).getAsJsonObject();

            taxRate = requiredDouble(tax, "rate");

            if (taxRate < 0) {
                throw new DomainValidationException(
                        id + ": negative tax rate");
            }
        }

        Order order = new Order(
                id,
                status,
                currency,
                customer,
                taxRate);

        for (int i = 0; i < itemsArray.size(); i++) {

            JsonObject itemObject =
                    itemsArray.get(i).getAsJsonObject();

            Item item = parseItem(itemObject);

            order.addItem(item);
        }

        return order;
    }

    private static Customer parseCustomer(JsonObject o)
            throws DomainValidationException {

        String id = requiredString(o, "customerId");

        JsonObject name =
                requiredObject(o, "name");

        String first =
                requiredString(name, "first");

        String last =
                requiredString(name, "last");

        String tier =
                requiredString(o, "loyaltyTier");

        return new Customer(id, first, last, tier);
    }

    private static Item parseItem(JsonObject o)
            throws DomainValidationException {

        String type = requiredString(o, "type");

        String sku = requiredString(o, "sku");

        String name = requiredString(o, "name");

        int qty = requiredInt(o, "qty");

        double price = requiredDouble(o, "price");

        if (qty <= 0) {
            throw new DomainValidationException(
                    sku + ": quantity must be positive");
        }

        if (price < 0) {
            throw new DomainValidationException(
                    sku + ": price cannot be negative");
        }

        Item item;

        if ("PHYSICAL".equals(type)) {

            double weight = 0.0;

            JsonObject weightObject =
                    o.getAsJsonObject("weight");

            if (weightObject != null) {
                weight = requiredDouble(
                        weightObject, "value");
            }

            item = new PhysicalItem(
                    sku, name, qty, price, weight);

        } else if ("DIGITAL".equals(type)) {

            String key = "";

            JsonObject license =
                    o.getAsJsonObject("license");

            if (license != null
                    && license.has("key")) {

                key = license.get("key").getAsString();
            }

            item = new DigitalItem(
                    sku, name, qty, price, key);

        } else if ("SERVICE".equals(type)) {

            String provider = "";

            JsonObject service =
                    o.getAsJsonObject("service");

            if (service != null
                    && service.has("provider")) {

                provider =
                        service.get("provider").getAsString();
            }

            item = new ServiceItem(
                    sku, name, qty, price, provider);

        } else {

            throw new DomainValidationException(
                    sku + ": unknown item type " + type);
        }

        JsonArray discounts =
                o.getAsJsonArray("discounts");

        if (discounts != null) {

            for (int i = 0; i < discounts.size(); i++) {

                JsonObject d =
                        discounts.get(i).getAsJsonObject();

                String code =
                        requiredString(d, "code");

                double amount =
                        requiredDouble(d, "amount");

                if (amount < 0) {
                    throw new DomainValidationException(
                            sku + ": negative discount");
                }

                item.addDiscount(
                        new Discount(code, amount));
            }
        }

        return item;
    }

    /*
     * Defensive helper methods.
     */

    private static String requiredString(
            JsonObject o, String field)
            throws DomainValidationException {

        if (!o.has(field)
                || o.get(field).isJsonNull()) {

            throw new DomainValidationException(
                    "Missing field: " + field);
        }

        if (!o.get(field).isJsonPrimitive()
                || !o.get(field)
                    .getAsJsonPrimitive()
                    .isString()) {

            throw new DomainValidationException(
                    "Field " + field
                    + " must be a string");
        }

        return o.get(field).getAsString();
    }

    private static double requiredDouble(
            JsonObject o, String field)
            throws DomainValidationException {

        if (!o.has(field)
                || o.get(field).isJsonNull()) {

            throw new DomainValidationException(
                    "Missing field: " + field);
        }

        if (!o.get(field).isJsonPrimitive()
                || !o.get(field)
                    .getAsJsonPrimitive()
                    .isNumber()) {

            throw new DomainValidationException(
                    "Field " + field
                    + " must be a number");
        }

        return o.get(field).getAsDouble();
    }

    private static int requiredInt(
            JsonObject o, String field)
            throws DomainValidationException {

        double value =
                requiredDouble(o, field);

        if (value != (int) value) {

            throw new DomainValidationException(
                    "Field " + field
                    + " must be an integer");
        }

        return (int) value;
    }

    private static JsonObject requiredObject(
            JsonObject o, String field)
            throws DomainValidationException {

        if (!o.has(field)
                || o.get(field).isJsonNull()
                || !o.get(field).isJsonObject()) {

            throw new DomainValidationException(
                    "Missing/invalid object: " + field);
        }

        return o.getAsJsonObject(field);
    }

    private static JsonArray requiredArray(
            JsonObject o, String field)
            throws DomainValidationException {

        if (!o.has(field)
                || o.get(field).isJsonNull()
                || !o.get(field).isJsonArray()) {

            throw new DomainValidationException(
                    "Missing/invalid array: " + field);
        }

        return o.getAsJsonArray(field);
    }
}