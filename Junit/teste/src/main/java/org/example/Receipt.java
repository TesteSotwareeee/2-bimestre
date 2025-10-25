package org.example;

import java.math.BigDecimal;

public final class Receipt {
    public final BigDecimal subtotal;
    public final BigDecimal discountPercentApplied;
    public final BigDecimal discountAmount;
    public final BigDecimal taxableBase;
    public final BigDecimal tax;
    public final BigDecimal shipping;
    public final BigDecimal total;

    public Receipt(BigDecimal subtotal, BigDecimal discountPercentApplied, BigDecimal discountAmount,
                   BigDecimal taxableBase, BigDecimal tax, BigDecimal shipping, BigDecimal total) {
        this.subtotal = subtotal; this.discountPercentApplied = discountPercentApplied; this.discountAmount = discountAmount;
        this.taxableBase = taxableBase; this.tax = tax; this.shipping = shipping; this.total = total;
    }
}
