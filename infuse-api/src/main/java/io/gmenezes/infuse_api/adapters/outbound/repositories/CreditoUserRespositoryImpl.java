package io.gmenezes.infuse_api.adapters.outbound.repositories;

import io.gmenezes.infuse_api.domain.credito.Credito;
import io.gmenezes.infuse_api.domain.credito.CreditoRepository;
import io.gmenezes.infuse_api.util.CreditoMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CreditoUserRespositoryImpl implements CreditoRepository {

    private final JpaCreditoRepository creditoRepository;

    @Override
    public List<Credito> findAllByNfse(String numeroNfse) {
        var creditos = creditoRepository.findAllByNfse(numeroNfse);
        return creditos.stream().map(CreditoMapper::fromJpaEntityToCredito).collect(Collectors.toList());
    }

    @Override
    public Credito findByNumeroCredito(String numeroCredito) {
        var credito = creditoRepository.findByNumeroCredito(numeroCredito);
        return CreditoMapper.fromJpaEntityToCredito(credito);
    }
}
