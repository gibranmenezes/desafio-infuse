package io.gmenezes.infuse_api.application.services;

import io.gmenezes.infuse_api.application.usecases.CreditoUseCases;
import io.gmenezes.infuse_api.application.usecases.EventUseCase;
import io.gmenezes.infuse_api.domain.credito.Credito;
import io.gmenezes.infuse_api.domain.credito.CreditoRepository;
import io.gmenezes.infuse_api.domain.credito.dtos.CreditoResponse;
import io.gmenezes.infuse_api.util.CreditoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditoUseCasesImpl implements CreditoUseCases {

    private final CreditoRepository creditoRepository;
    private final EventUseCase eventService;


    @Autowired
    private CreditoMapper creditoMapper;

    @Override
    public List<CreditoResponse> getCreditosByNfse(String numeroNfse) {
        try {
            var creditos = creditoRepository.findAllByNfse(numeroNfse);
            List<CreditoResponse> resultado = creditos.stream()
                    .map(creditoMapper::fromCreditoToResponse)
                    .toList();

            Map<String, Object> detalhes = new HashMap<>();
            detalhes.put("quantidade", resultado.size());
            eventService.registrarConsultaNfse(numeroNfse, !resultado.isEmpty(), detalhes);

            return resultado;
        } catch (Exception e) {
            Map<String, Object> detalhes = new HashMap<>();
            detalhes.put("erro", e.getMessage());
            eventService.registrarConsultaNfse(numeroNfse, false, detalhes);
            throw e;
        }
    }

    @Override
    public CreditoResponse getCreditoByNumero(String numeroCredito) {
        try {
            Credito credito = creditoRepository.findByNumeroCredito(numeroCredito);
            if (credito == null) {
                Map<String, Object> detalhes = new HashMap<>();
                detalhes.put("mensagem", "Crédito não encontrado");
                eventService.registrarConsultaCredito(numeroCredito, false, detalhes);

                throw new ObjectNotFoundException("Crédito não encontrado para o numero: " + numeroCredito, Credito.class);
            }

            CreditoResponse response = creditoMapper.fromCreditoToResponse(credito);

            Map<String, Object> detalhes = new HashMap<>();
            detalhes.put("creditoId", credito.getNumeroCredito());
            eventService.registrarConsultaCredito(numeroCredito, true, detalhes);

            return response;
        } catch (Exception e) {
            if (!(e instanceof ObjectNotFoundException)) {
                Map<String, Object> detalhes = new HashMap<>();
                detalhes.put("erro", e.getMessage());
                eventService.registrarConsultaCredito(numeroCredito, false, detalhes);
            }
            throw e;
        }
    }

}
