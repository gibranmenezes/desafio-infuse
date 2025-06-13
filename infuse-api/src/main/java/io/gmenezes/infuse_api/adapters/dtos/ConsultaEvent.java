package io.gmenezes.infuse_api.adapters.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaEvent {
    private String id;
    private LocalDateTime timestamp;
    private String tipoConsulta;
    private String resultado;
    private Object detalhes;

}
