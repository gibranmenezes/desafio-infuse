package io.gmenezes.infuse_api.application.services;

import io.gmenezes.infuse_api.application.usecases.CreditoUseCases;
import io.gmenezes.infuse_api.domain.credito.Credito;
import io.gmenezes.infuse_api.domain.credito.CreditoRepository;
import io.gmenezes.infuse_api.domain.credito.dtos.CreditoResponse;
import io.gmenezes.infuse_api.util.CreditoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditoUseCasesImpl implements CreditoUseCases {

    private final CreditoRepository creditoRepository;

    @Override
    public List<CreditoResponse> getCreditosByNfse(String numeroNfse) {
        var creditos = creditoRepository.findAllByNfse(numeroNfse);
        return creditos.stream()
                .map(CreditoMapper::fromCreditoToResponse)
                .toList();
    }

    @Override
    public Optional<CreditoResponse> getCreditoByNumero(String numeroCredito) {
        return Optional.empty();
    }
}
