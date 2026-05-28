package com.creditrecovery.infrastructure.adapter.persistence;

import com.creditrecovery.domain.model.Devedor;
import com.creditrecovery.domain.model.Divida;
import com.creditrecovery.domain.model.StatusDivida;

class DividaMapper {

    static DividaEntity paraEntidade(Divida divida) {
        DevedorEntity devedorEntity = new DevedorEntity(
                divida.devedor().id(),
                divida.devedor().nome(),
                divida.devedor().documento());
        return new DividaEntity(
                divida.id(),
                devedorEntity,
                divida.valorOriginal(),
                divida.dataVencimento(),
                divida.status());
    }

    static Divida paraDominio(DividaEntity entity) {
        Devedor devedor = new Devedor(
                entity.getDevedor().getId(),
                entity.getDevedor().getNome(),
                entity.getDevedor().getDocumento());

        Divida divida = new Divida(
                entity.getId(),
                devedor,
                entity.getValorOriginal(),
                entity.getDataVencimento());

        // restaura o status — ele não é REGISTRADA por padrão, é o que está no banco
        if (entity.getStatus() != StatusDivida.REGISTRADA) {
            forcarStatus(divida, entity.getStatus());
        }
        return divida;
    }

    // pula a validação de transição porque estamos restaurando estado persistido
    private static void forcarStatus(Divida divida, StatusDivida status) {
        try {
            var f = Divida.class.getDeclaredField("status");
            f.setAccessible(true);
            f.set(divida, status);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Falha ao restaurar status", e);
        }
    }
}