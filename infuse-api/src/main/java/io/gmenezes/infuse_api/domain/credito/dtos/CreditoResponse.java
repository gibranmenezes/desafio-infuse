package io.gmenezes.infuse_api.domain.credito.dtos;

import io.gmenezes.infuse_api.domain.credito.Credito;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public record CreditoResponse(String numeroCredito,
                              String numeroNfse,
                              String dataConstituicao,
                              BigDecimal valorIssqn,
                              String tipoCredito,
                              boolean simplesNacional,
                              BigDecimal  aliquota,
                              BigDecimal  valorFaturado,
                              BigDecimal  valorDeducao,
                              BigDecimal  baseCalculo) {

    public CreditoResponse(Credito credito) {
        this(credito.getNumeroCredito(), credito.getNumeroNfse(),
                credito.getDataConstituicao().format(DateTimeFormatter.ISO_DATE),
                credito.getValorIssqn(), credito.getTipoCredito(), credito.isSimplesNacional(),
                credito.getAliquota(), credito.getValorFaturado(), credito.getValorDeducao(), credito.getBaseCalculo());
    }
}
