package io.gmenezes.infuse_api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gmenezes.infuse_api.domain.credito.Credito;
import io.gmenezes.infuse_api.domain.credito.dtos.CreditoResponse;

public interface CreditoMapper {

    ObjectMapper mapper = new ObjectMapper();

    static CreditoResponse fromCreditoToResponse (Credito credito) {
        return mapper.convertValue(credito, CreditoResponse.class);
    }
}
