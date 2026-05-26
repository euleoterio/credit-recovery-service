package com.creditrecovery.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Divida {

    private final UUID id;
    private final Devedor devedor;
    private final BigDecimal valorOriginal;
    private final LocalDate dataVencimento;
    private StatusDivida status;

    public Divida(UUID id, Devedor devedor,
                  BigDecimal valorOriginal, LocalDate dataVencimento) {
        this.id = Objects.requireNonNull(id, "id é obrigatório");
        this.devedor = Objects.requireNonNull(devedor, "devedor é obrigatório");
        this.dataVencimento =
                Objects.requireNonNull(dataVencimento, "vencimento é obrigatório");

        Objects.requireNonNull(valorOriginal, "valor é obrigatório");
        if (valorOriginal.signum() <= 0) {
            throw new IllegalArgumentException("valor deve ser positivo");
        }
        this.valorOriginal = valorOriginal;

        this.status = StatusDivida.REGISTRADA;
    }

    public void transicionarPara(StatusDivida destino) {
        Objects.requireNonNull(destino, "status destino é obrigatório");
        if (!status.podeTransicionarPara(destino)) {
            throw new TransicaoInvalidaException(status, destino);
        }
        this.status = destino;
    }

    public UUID id() { return id; }
    public Devedor devedor() { return devedor; }
    public BigDecimal valorOriginal() { return valorOriginal; }
    public LocalDate dataVencimento() { return dataVencimento; }
    public StatusDivida status() { return status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Divida outra)) return false;
        return id.equals(outra.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}