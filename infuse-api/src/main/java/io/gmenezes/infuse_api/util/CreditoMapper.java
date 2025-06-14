package io.gmenezes.infuse_api.util;

import io.gmenezes.infuse_api.adapters.outbound.entities.JpaCreditoEntity;
import io.gmenezes.infuse_api.domain.credito.Credito;
import io.gmenezes.infuse_api.domain.credito.dtos.CreditoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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

    @Mapping(target = "numeroCredito", source = "numeroCredito")
    @Mapping(target = "numeroNfse", source = "numeroNfse")
    @Mapping(target = "dataConstituicao", source = "dataConstituicao")
    @Mapping(target = "valorIssqn", source = "valorIssqn")
    @Mapping(target = "tipoCredito", source = "tipoCredito")
    @Mapping(target = "simplesNacional", source = "simplesNacional")
    @Mapping(target = "aliquota", source = "aliquota")
    @Mapping(target = "valorFaturado", source = "valorFaturado")
    @Mapping(target = "valorDeducao", source = "valorDeducao")
    @Mapping(target = "baseCalculo", source = "baseCalculo")
    Credito fromJpaEntityToCredito(JpaCreditoEntity jpaCreditoEntity);

}
