package com.creditrecovery.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;

public class DividaTest {

    private Divida novaDivida() {
        Devedor devedor = new Devedor(
                UUID.randomUUID(), "Maria Silva", "12345678901");

        return new Divida(
                UUID.randomUUID(), devedor, new BigDecimal("1500.00"), LocalDate.now().minusDays(30));
    }

    @Test
    @DisplayName("dívida  nova nasce no status REGISTRADA")
    void dividaNasceNoStatusRegistrada() {
        Divida divida = novaDivida();

        assertThat(divida.status()).isEqualTo(StatusDivida.REGISTRADA);
    }

    @Test
    @DisplayName("transição válida muda o status da dívida")
    void transicaoValida() {
        Divida divida = novaDivida();
        divida.transicionarPara(StatusDivida.EM_COBRANCA);

        assertThat(divida.status()).isEqualTo(StatusDivida.EM_COBRANCA);
    }

    @Test
    @DisplayName("transição inválida lança TransicaoInvalidaException")
    void transicaoInvalida() {
        Divida divida = novaDivida(); // está em REGISTRADA

        assertThatThrownBy(() ->
                divida.transicionarPara(StatusDivida.QUITADA))
                .isInstanceOf(TransicaoInvalidaException.class)
                .hasMessageContaining("REGISTRADA")
                .hasMessageContaining("QUITADA");
    }

    @Test
    @DisplayName("fluxo completo até QUITADA percorre estados válidos")
    void fluxoCompletoAteQuitada() {
        Divida divida = novaDivida();

        assertThatCode(() -> {
            divida.transicionarPara(StatusDivida.EM_COBRANCA);
            divida.transicionarPara(StatusDivida.EM_NEGOCIACAO);
            divida.transicionarPara(StatusDivida.ACORDADA);
            divida.transicionarPara(StatusDivida.QUITADA);
        }).doesNotThrowAnyException();

        assertThat(divida.status()).isEqualTo(StatusDivida.QUITADA);
    }

    @Test
    @DisplayName("QUITADA é estado terminal, não permite saída")
    void quitadaEhTerminal() {
        Divida divida = novaDivida();
        divida.transicionarPara(StatusDivida.EM_COBRANCA);
        divida.transicionarPara(StatusDivida.QUITADA);

        assertThat(divida.status().ehTerminal()).isTrue();
        assertThatThrownBy(() ->
                divida.transicionarPara(StatusDivida.EM_COBRANCA))
                .isInstanceOf(TransicaoInvalidaException.class);
    }
}
