package io.gmenezes.infuse_api.adapters.outbound.repositories;

import io.gmenezes.infuse_api.domain.credito.Credito;
import io.gmenezes.infuse_api.domain.credito.CreditoRepository;
import io.gmenezes.infuse_api.util.CreditoMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class CreditoUserRespositoryImpl implements CreditoRepository {

    private final JpaCreditoRepository creditoRepository;
    private final CreditoMapper creditoMapper;

    @Override
    public List<Credito> findAllByNfse(String numeroNfse) {
        var creditos = creditoRepository.findAllByNfse(numeroNfse);
        return creditos.stream().map(creditoMapper::fromJpaEntityToCredito).collect(Collectors.toList());
    }

    @Override
    public Optional<Credito> findByNumeroCredito(String numeroCredito) {
        return Optional.empty();
    }
}
