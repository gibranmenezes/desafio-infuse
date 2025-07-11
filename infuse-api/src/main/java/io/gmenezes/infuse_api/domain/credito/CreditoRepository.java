package io.gmenezes.infuse_api.domain.credito;

import java.util.List;
import java.util.Optional;

public interface CreditoRepository {

    List<Credito> findAllByNfse(String numeroNfse);
    Credito findByNumeroCredito(String numeroCredito);
}
