package io.gmenezes.infuse_api.domain.event;

import io.gmenezes.infuse_api.adapters.dtos.ConsultaEvent;

public interface EventPublisher {

    void publicarEvento(ConsultaEvent event);
}
