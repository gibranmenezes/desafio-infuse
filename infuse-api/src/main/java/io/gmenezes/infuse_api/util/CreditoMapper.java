package io.gmenezes.infuse_api.util;

import io.gmenezes.infuse_api.adapters.outbound.entities.JpaCreditoEntity;
import io.gmenezes.infuse_api.domain.credito.Credito;
import io.gmenezes.infuse_api.domain.credito.dtos.CreditoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreditoMapper {

    @Mapping(target = "dataConstituicao", expression = "credito.getDataConstituicao().format(java.time.format.DateTimeFormatter.ofPattern(\"yyyy-MM-dd\"))")
    @Mapping(target = "simplesNacional", expression = "java(credito.isSimplesNacional() ? \"Sim\" : \"Não\")")
    CreditoResponse fromCreditoToResponse(Credito credito);


    Credito fromJpaEntityToCredito(JpaCreditoEntity jpaCreditoEntity);

}
