package org.example;

import java.time.LocalDate;

public final class Coupon {
    public enum Type { PERCENT, FRETE_GRATIS }

    private final String code;
    private final Type type;
    private final double percent;
    private final Double minSubtotal;
    private final LocalDate expiresAt;
    private final Double maxWeight;

    private Coupon(String code, Type type, double percent, Double minSubtotal, LocalDate expiresAt, Double maxWeight) {
        this.code = code; this.type = type; this.percent = percent;
        this.minSubtotal = minSubtotal; this.expiresAt = expiresAt; this.maxWeight = maxWeight;
    }

    public static Coupon desc10() { return new Coupon("DESC10", Type.PERCENT, 0.10, null, null, null); }
    public static Coupon desc20(LocalDate expiresAt) { return new Coupon("DESC20", Type.PERCENT, 0.20, 100.0, expiresAt, null); }
    public static Coupon freteGratis() { return new Coupon("FRETEGRATIS", Type.FRETE_GRATIS, 0.0, null, null, 5.0); }

    public String code() { return code; }
    public Type type() { return type; }
    public double percent() { return percent; }
    public Double minSubtotal() { return minSubtotal; }
    public LocalDate expiresAt() { return expiresAt; }
    public Double maxWeight() { return maxWeight; }
}
