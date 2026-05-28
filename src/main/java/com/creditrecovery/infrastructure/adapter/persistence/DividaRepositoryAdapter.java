package com.creditrecovery.infrastructure.adapter.persistence;

import com.creditrecovery.domain.model.Divida;
import com.creditrecovery.domain.port.DividaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
class DividaRepositoryAdapter implements DividaRepository {

    private final DividaJpaRepository jpaRepository;

    DividaRepositoryAdapter(DividaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Divida salvar(Divida divida) {
        DividaEntity entity = DividaMapper.paraEntidade(divida);
        DividaEntity salva = jpaRepository.save(entity);
        return DividaMapper.paraDominio(salva);
    }

    @Override
    public Optional<Divida> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(DividaMapper::paraDominio);
    }
}