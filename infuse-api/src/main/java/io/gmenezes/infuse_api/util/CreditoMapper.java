package io.gmenezes.infuse_api.util;

import io.gmenezes.infuse_api.adapters.outbound.entities.JpaCreditoEntity;
import io.gmenezes.infuse_api.domain.credito.Credito;
import io.gmenezes.infuse_api.domain.credito.dtos.CreditoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CreditoMapper {


    @Mapping(target = "numeroCredito")
    @Mapping(target = "numeroNfse")
    @Mapping(target = "dataConstituicao", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "valorIssqn")
    @Mapping(target = "tipoCredito")
    @Mapping(target = "simplesNacional", expression = "java(credito.isSimplesNacional() ? \"Sim\" : \"Não\")")
    @Mapping(target = "aliquota")
    @Mapping(target = "valorFaturado")
    @Mapping(target = "valorDeducao")
    @Mapping(target = "baseCalculo")
    CreditoResponse fromCreditoToResponse(Credito credito);

    Credito fromJpaEntityToCredito(JpaCreditoEntity jpaCreditoEntity);

}
