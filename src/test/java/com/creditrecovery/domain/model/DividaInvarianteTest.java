package com.creditrecovery.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DividaInvarianteTest {

    private Devedor devedorValido() {
        return new Devedor(UUID.randomUUID(), "João Souza", "98765432100");
    }

    @Test
    @DisplayName("dívida com valor zero é rejeitada")
    void valorZeroRejeitado() {
        assertThatThrownBy(() -> new Divida(
                UUID.randomUUID(), devedorValido(),
                BigDecimal.ZERO, LocalDate.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positivo");
    }

    @Test
    @DisplayName("dívida com valor negativo é rejeitada")
    void valorNegativoRejeitado() {
        assertThatThrownBy(() -> new Divida(
                UUID.randomUUID(), devedorValido(),
                new BigDecimal("-10.00"), LocalDate.now()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("dívida sem devedor é rejeitada")
    void devedorNuloRejeitado() {
        assertThatThrownBy(() -> new Divida(
                UUID.randomUUID(), null,
                new BigDecimal("100.00"), LocalDate.now()))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("devedor");
    }
}