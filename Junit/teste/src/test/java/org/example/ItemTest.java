package org.example;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    @Test
    void deveCalcularLineTotalCorreto() {
        Item item = new Item(Category.OTHER, new BigDecimal("10.00"), 3);
        assertEquals(new BigDecimal("30.00"), item.lineTotal());
    }

    @Test
    void deveLancarExcecaoSePrecoNegativo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Item(Category.OTHER, new BigDecimal("-1.00"), 1);
        });
    }

    @Test
    void deveLancarExcecaoSeQuantidadeZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Item(Category.OTHER, new BigDecimal("5.00"), 0);
        });
    }
}
