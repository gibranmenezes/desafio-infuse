package io.gmenezes.infuse_api.adapters.outbound.repositories;

import io.gmenezes.infuse_api.adapters.outbound.entities.JpaCreditoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaCreditoRepository extends JpaRepository<JpaCreditoEntity, Long> {

    @Query("""
            SELECT c FROM JpaCreditoEntity c 
            WHERE c.numeroNfse = :numeroNfse
            """)
    List<JpaCreditoEntity> findAllByNfse(String numeroNfse);
}
