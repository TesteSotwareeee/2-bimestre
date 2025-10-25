package org.example;

import java.math.BigDecimal;

public final class Item {
    private final Category category;
    private final BigDecimal unitPrice;
    private final int quantity;

    public Item(Category category, BigDecimal unitPrice, int quantity) {
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("precoUnitario < 0");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantidade ≤ 0");
        }
        this.category = category;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public Category category() { return category; }
    public BigDecimal unitPrice() { return unitPrice; }
    public int quantity() { return quantity; }

    public BigDecimal lineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
