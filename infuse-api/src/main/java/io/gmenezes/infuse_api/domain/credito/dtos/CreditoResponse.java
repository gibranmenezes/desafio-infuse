package io.gmenezes.infuse_api.domain.credito.dtos;

import java.math.BigDecimal;

public record CreditoResponse(String numeroCredito,
                              String numeroNfse,
                              String dataConstituicao,
                              BigDecimal valorIssqn,
                              String tipoCredito,
                              String simplesNacional,
                              BigDecimal  aliquota,
                              BigDecimal  valorFaturado,
                              BigDecimal  valorDeducao,
                              BigDecimal  baseCalculo) {


}
