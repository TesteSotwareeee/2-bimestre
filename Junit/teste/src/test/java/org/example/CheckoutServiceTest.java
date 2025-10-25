package org.example;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CheckoutServiceTest {

    private static Item item(Category c, String price, int qty) {
        return new Item(c, new BigDecimal(price), qty);
    }

    @Test
    void fluxoBasico() {
        var svc = new CheckoutService();
        var r = svc.checkout(
                List.of(item(Category.OTHER,"100.00",1)),
                Tier.BASIC, false, Optional.empty(),
                Region.SUL, 1.0, LocalDate.now()
        );
        assertEquals(new BigDecimal("100.00"), r.subtotal);
        assertEquals(new BigDecimal("12.00"), r.tax);
        assertEquals(new BigDecimal("20.00"), r.shipping);
        assertEquals(new BigDecimal("132.00"), r.total);
    }

    @Test
    void aplicaDESC10() {
        var svc = new CheckoutService();
        var r = svc.checkout(
                List.of(item(Category.OTHER,"200.00",1)),
                Tier.BASIC, false, Optional.of(Coupon.desc10()),
                Region.SUL, 1.0, LocalDate.now()
        );
        assertEquals(new BigDecimal("20.00"), r.discountAmount);
        assertEquals(new BigDecimal("21.60"), r.tax);
        assertEquals(new BigDecimal("20.00"), r.shipping);
        assertEquals(new BigDecimal("221.60"), r.total);
    }

    @Test
    void teto30() {
        var svc = new CheckoutService();
        var r = svc.checkout(
                List.of(item(Category.OTHER,"200.00",1)),
                Tier.GOLD, true, Optional.of(Coupon.desc20(LocalDate.now().plusDays(1))),
                Region.SUDESTE, 1.0, LocalDate.now()
        );
        assertEquals(0.30, r.discountPercentApplied.doubleValue(), 1e-9);
    }

    @Test
    void freteGratisCupomPesoAte5() {
        var svc = new CheckoutService();
        var r = svc.checkout(
                List.of(item(Category.OTHER,"120.00",1)),
                Tier.BASIC, false, Optional.of(Coupon.freteGratis()),
                Region.NORTE, 5.0, LocalDate.now()
        );
        assertEquals(new BigDecimal("0.00"), r.shipping);
    }

    @Test
    void bookIsento() {
        var svc = new CheckoutService();
        var r = svc.checkout(
                List.of(item(Category.BOOK,"150.00",1), item(Category.OTHER,"50.00",1)),
                Tier.SILVER, false, Optional.empty(),
                Region.OUTRAS, 3.0, LocalDate.now()
        );
        assertEquals(new BigDecimal("5.70"), r.tax); // 50 com 5% desc -> 47.50 * 12% = 5.70
    }
}
