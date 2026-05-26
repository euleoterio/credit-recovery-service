package com.creditrecovery.domain.model;

import java.util.Set;

public enum StatusDivida {
    REGISTRADA,
    EM_COBRANCA,
    EM_NEGOCIACAO,
    ACORDADA,
    QUITADA,
    INADIMPLENTE_CRITICA;

    private Set<StatusDivida> transicoesPermitidas;

    static {
        REGISTRADA.transicoesPermitidas = Set.of(EM_COBRANCA, INADIMPLENTE_CRITICA);
        EM_COBRANCA.transicoesPermitidas = Set.of(EM_NEGOCIACAO, QUITADA, INADIMPLENTE_CRITICA);
        EM_NEGOCIACAO.transicoesPermitidas = Set.of(ACORDADA, EM_COBRANCA, INADIMPLENTE_CRITICA);
        ACORDADA.transicoesPermitidas = Set.of(QUITADA, INADIMPLENTE_CRITICA);
        QUITADA.transicoesPermitidas = Set.of();
        INADIMPLENTE_CRITICA.transicoesPermitidas = Set.of();
    }

    public boolean podeTransicionarPara(StatusDivida destino) {
        return transicoesPermitidas.contains(destino);
    }

    public boolean ehTerminal() {
        return transicoesPermitidas.isEmpty();
    }

}
