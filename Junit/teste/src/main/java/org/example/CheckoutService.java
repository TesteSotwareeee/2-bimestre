package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class CheckoutService {

    public Receipt checkout(List<Item> items,
                            Tier tier,
                            boolean primeiraCompra,
                            Optional<Coupon> couponOpt,
                            Region region,
                            double pesoTotal,
                            LocalDate today) {

        Objects.requireNonNull(items, "items");
        Objects.requireNonNull(tier, "tier");
        Objects.requireNonNull(region, "region");
        if (pesoTotal < 0) throw new IllegalArgumentException("peso < 0");

        BigDecimal subtotal = items.stream().map(Item::lineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

        // Fidelidade (sem switch moderno)
        double tierPct;
        if (tier == Tier.BASIC) {
            tierPct = 0.0;
        } else if (tier == Tier.SILVER) {
            tierPct = 0.05;
        } else if (tier == Tier.GOLD) {
            tierPct = 0.10;
        } else {
            tierPct = 0.0;
        }

        double firstBuyPct = (primeiraCompra && subtotal.compareTo(new BigDecimal("50.00")) >= 0) ? 0.05 : 0.0;

        double couponPct = 0.0;
        boolean freteGratisCupom = false;

        if (couponOpt != null && couponOpt.isPresent()) {
            Coupon c = couponOpt.get();
            boolean valido = true;
            if (c.expiresAt() != null && today != null) {
                valido = !today.isAfter(c.expiresAt());
            }
            if (valido && c.minSubtotal() != null) {
                valido = subtotal.compareTo(BigDecimal.valueOf(c.minSubtotal())) >= 0;
            }
            if (valido) {
                if (c.type() == Coupon.Type.PERCENT) {
                    couponPct = c.percent();
                } else if (c.type() == Coupon.Type.FRETE_GRATIS) {
                    freteGratisCupom = c.maxWeight() == null || pesoTotal <= c.maxWeight();
                }
            }
        }

        double cappedPct = Math.min(tierPct + firstBuyPct + couponPct, 0.30);

        BigDecimal discountAmount = subtotal.multiply(BigDecimal.valueOf(cappedPct));
        BigDecimal subtotalWithDiscount = subtotal.subtract(discountAmount);

        BigDecimal baseNaoBook = items.stream()
                .filter(i -> i.category() != Category.BOOK)
                .map(Item::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal taxableBase = baseNaoBook.multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(cappedPct)));
        BigDecimal tax = taxableBase.multiply(new BigDecimal("0.12"));

        boolean freteGratisPorValor = subtotal.compareTo(new BigDecimal("300.00")) >= 0;
        BigDecimal shipping = BigDecimal.ZERO;
        if (!freteGratisPorValor && !freteGratisCupom) {
            shipping = calcularFrete(region, pesoTotal);
        }

        BigDecimal total = subtotalWithDiscount.add(tax).add(shipping);

        return new Receipt(
                round(subtotal),
                BigDecimal.valueOf(cappedPct),
                round(discountAmount),
                round(taxableBase),
                round(tax),
                round(shipping),
                round(total)
        );
    }

    private static BigDecimal calcularFrete(Region region, double peso) {
        if (region == Region.SUL || region == Region.SUDESTE) {
            if (peso <= 2) return bd("20");
            if (peso <= 5) return bd("35");
            return bd("50");
        } else if (region == Region.NORTE) {
            if (peso <= 2) return bd("30");
            if (peso <= 5) return bd("55");
            return bd("80");
        } else {
            return bd("40");
        }
    }

    private static BigDecimal bd(String s) { return new BigDecimal(s); }
    private static BigDecimal round(BigDecimal v) { return v.setScale(2, RoundingMode.HALF_UP); }
}
