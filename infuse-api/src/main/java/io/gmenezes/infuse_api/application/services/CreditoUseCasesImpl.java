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
    private final CreditoMapper creditoMapper;

    @Override
    public List<CreditoResponse> getCreditosByNfse(String numeroNfse) {
            var creditos = creditoRepository.findAllByNfse(numeroNfse);
            List<CreditoResponse> resultado = creditos.stream()
                    .map(creditoMapper::fromCreditoToResponse)
                    .toList();
            publicarEventoNfse(resultado);

            return resultado;
    }

    @Override
    public CreditoResponse getCreditoByNumero(String numeroCredito) {
            Credito credito = creditoRepository.findByNumeroCredito(numeroCredito);
            if (credito == null) {
                publicarEventoCredito(numeroCredito, false);
                throw new ObjectNotFoundException("Crédito não encontrado para o numero: " + numeroCredito, Credito.class);
            }

            publicarEventoCredito(numeroCredito, true);

            return creditoMapper.fromCreditoToResponse(credito);

    }

    private void publicarEventoNfse(List<CreditoResponse> responses) {
        log.debug("Publicando evento para NFSe {}", responses.getFirst().numeroNfse());
        Map<String, Object> detalhes = new HashMap<>();
        detalhes.put("quantidade", responses.size());
        eventService.registrarConsultaNfse(responses.getFirst().numeroNfse(), !responses.isEmpty(), detalhes);
    }

    private void publicarEventoCredito(String numeroCredito, boolean sucesso) {
        log.debug("Publicando evento para Numero credito {}", numeroCredito);
        Map<String, Object> detalhes = new HashMap<>();
        detalhes.put("creditoId",numeroCredito);
        eventService.registrarConsultaCredito(numeroCredito, sucesso, detalhes);
    }

}
