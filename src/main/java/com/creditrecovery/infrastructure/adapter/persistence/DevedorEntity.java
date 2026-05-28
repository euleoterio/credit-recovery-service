package com.creditrecovery.infrastructure.adapter.persistence;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "devedor")
public class DevedorEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String documento;

    protected DevedorEntity() {} //exigido pelo JPA

    public DevedorEntity(UUID id, String nome, String documento) {
        this.id = id;
        this.nome = nome;
        this.documento = documento;
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getDocumento() { return documento; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DevedorEntity outro)) return false;
        return id != null && id.equals(outro.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
