package com.creditrecovery.domain.port;

import com.creditrecovery.domain.model.Divida;
import java.util.Optional;
import java.util.UUID;

public interface DividaRepository {

    Divida salvar(Divida divida);

    Optional<Divida> buscarPorId(UUID id);
}