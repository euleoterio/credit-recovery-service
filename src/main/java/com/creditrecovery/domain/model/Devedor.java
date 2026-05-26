package com.creditrecovery.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Devedor {

    private final UUID id;
    private final String nome;
    private final String documento; // CPF ou CNPJ

    public Devedor(UUID id, String nome, String documento) {
        this.id = Objects.requireNonNull(id, "id é obrigatório");
        this.nome = Objects.requireNonNull(nome, "nome é obrigatório");
        this.documento = Objects.requireNonNull(documento, "documento é obrigatório");
    }

    public UUID id() { return id; }
    public String nome() { return nome; }
    public String documento() { return documento; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Devedor outro)) return false;
        return id.equals(outro.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}