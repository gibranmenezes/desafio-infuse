package io.gmenezes.infuse_api.application.services;

import io.gmenezes.infuse_api.adapters.dtos.ConsultaEvent;
import io.gmenezes.infuse_api.application.usecases.EventUseCase;
import io.gmenezes.infuse_api.domain.event.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class EvenUseCaseImpl implements EventUseCase {

    private final EventPublisher eventPublisher;


    @Override
    public void registrarConsultaNfse(String numeroNfse, boolean sucesso, Object detalhes) {
        try {
            ConsultaEvent event = ConsultaEvent.builder()
                    .id(UUID.randomUUID().toString())
                    .timestamp(LocalDateTime.now())
                    .tipoConsulta("nfse")
                    .resultado(sucesso ? "sucesso" : "erro")
                    .detalhes(detalhes)
                    .build();

            eventPublisher.publicarEvento(event);
        } catch (Exception e) {
            log.error("Erro ao registrar evento de consulta NFSE: {}", e.getMessage());
        }

    }

    @Override
    public void registrarConsultaCredito(String numeroCredito, boolean sucesso, Object detalhes) {
        try {
            ConsultaEvent event = ConsultaEvent.builder()
                    .id(UUID.randomUUID().toString())
                    .timestamp(LocalDateTime.now())
                    .tipoConsulta("credito")
                    .resultado(sucesso ? "sucesso" : "erro")
                    .detalhes(detalhes)
                    .build();

            eventPublisher.publicarEvento(event);
        } catch (Exception e) {
            log.error("Erro ao registrar evento de consulta de crédito: {}", e.getMessage());
        }

    }
}
