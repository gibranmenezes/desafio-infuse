package io.gmenezes.infuse_api.application.services;

import io.gmenezes.infuse_api.application.usecases.CreditoUseCases;
import io.gmenezes.infuse_api.domain.credito.Credito;
import io.gmenezes.infuse_api.domain.credito.CreditoRepository;
import io.gmenezes.infuse_api.domain.credito.dtos.CreditoResponse;
import io.gmenezes.infuse_api.util.CreditoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditoUseCasesImpl implements CreditoUseCases {

    private final CreditoRepository creditoRepository;

    @Autowired
    private CreditoMapper creditoMapper;

    @Override
    public List<CreditoResponse> getCreditosByNfse(String numeroNfse) {
        var creditos = creditoRepository.findAllByNfse(numeroNfse);
        return creditos.stream()
                .map(creditoMapper::fromCreditoToResponse)
                .toList();
    }

    @Override
    public CreditoResponse getCreditoByNumero(String numeroCredito) {
        Credito credito = creditoRepository.findByNumeroCredito(numeroCredito);
        if (credito == null) {
            throw new ObjectNotFoundException("Crédito não encontrado para o numero: " + numeroCredito, Credito.class);
        }
        return creditoMapper.fromCreditoToResponse(credito);
    }

}
