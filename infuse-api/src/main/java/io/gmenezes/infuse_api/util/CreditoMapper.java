package io.gmenezes.infuse_api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gmenezes.infuse_api.adapters.outbound.entities.JpaCreditoEntity;
import io.gmenezes.infuse_api.domain.credito.Credito;
import io.gmenezes.infuse_api.domain.credito.dtos.CreditoResponse;

import java.time.format.DateTimeFormatter;

public interface CreditoMapper {


    static CreditoResponse fromCreditoToResponse(Credito credito) {
        return new CreditoResponse(
                credito.getNumeroCredito(),
                credito.getNumeroNfse(),
                credito.getDataConstituicao().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                credito.getValorIssqn(),
                credito.getTipoCredito(),
                credito.isSimplesNacional() ? "Sim" : "Não",
                credito.getAliquota(),
                credito.getValorFaturado(),
                credito.getValorDeducao(),
                credito.getBaseCalculo()
        );
    }


    static Credito fromJpaEntityToCredito(JpaCreditoEntity jpaCreditoEntity) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(jpaCreditoEntity, Credito.class);
    }

}
