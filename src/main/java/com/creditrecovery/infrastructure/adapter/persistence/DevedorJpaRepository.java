package com.creditrecovery.infrastructure.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DevedorJpaRepository extends JpaRepository<DevedorEntity, UUID> {
}