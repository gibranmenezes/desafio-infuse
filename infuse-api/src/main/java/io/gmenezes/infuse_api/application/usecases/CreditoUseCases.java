package io.gmenezes.infuse_api.application.usecases;

import io.gmenezes.infuse_api.domain.credito.dtos.CreditoResponse;

import java.util.List;
import java.util.Optional;

public interface CreditoUseCases {

    List<CreditoResponse> getCreditosByNfse(String numeroNfse);
    CreditoResponse getCreditoByNumero(String numeroCredito);
}
