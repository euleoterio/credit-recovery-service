package com.creditrecovery.domain.model;

public class TransicaoInvalidaException extends RuntimeException {

    public TransicaoInvalidaException (StatusDivida origem, StatusDivida destino) {
        super("Transição inválida: não [e érmitido ir de " + origem + " para " + destino + ".");
    }
}
