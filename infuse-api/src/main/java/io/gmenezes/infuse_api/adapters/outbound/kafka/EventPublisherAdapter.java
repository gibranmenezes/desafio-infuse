package io.gmenezes.infuse_api.adapters.outbound.kafka;

import io.gmenezes.infuse_api.adapters.dtos.ConsultaEvent;
import io.gmenezes.infuse_api.domain.event.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventPublisherAdapter implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topic.consulta-creditos}")
    private String consultaTopic;


    @Override
    public void publicarEvento(ConsultaEvent event) {
        CompletableFuture<?> future = kafkaTemplate.send(consultaTopic, event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Evento de consulta enviado com sucesso para o tópico {}", consultaTopic);
            } else {
                log.error("Falha ao enviar evento de consulta para o tópico {}: {}",
                        consultaTopic, ex.getMessage());
            }
        });
    }
}
