package com.creditrecovery.infrastructure.adapter.persistence;

import com.creditrecovery.domain.model.StatusDivida;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "divida")
public class DividaEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "devedor_id", nullable = false)
    private DevedorEntity devedor;

    @Column(name = "valor_original", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorOriginal;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusDivida status;

    protected DividaEntity() {}

    public DividaEntity(UUID id, DevedorEntity devedor, BigDecimal valorOriginal,
                        LocalDate dataVencimento, StatusDivida status) {
        this.id = id;
        this.devedor = devedor;
        this.valorOriginal = valorOriginal;
        this.dataVencimento = dataVencimento;
        this.status = status;
    }

    public UUID getId() { return id; }
    public DevedorEntity getDevedor() { return devedor; }
    public BigDecimal getValorOriginal() { return valorOriginal; }
    public LocalDate getDataVencimento() { return dataVencimento; }
    public StatusDivida getStatus() { return status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DividaEntity outra)) return false;
        return id != null && id.equals(outra.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}