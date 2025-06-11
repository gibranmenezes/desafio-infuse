package io.gmenezes.infuse_api.adapters.inbound.controller;

import io.gmenezes.infuse_api.adapters.dtos.AppResponse;
import io.gmenezes.infuse_api.application.services.CreditoUseCasesImpl;
import io.gmenezes.infuse_api.domain.credito.dtos.CreditoResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController ("/api/creditos/")
@RequiredArgsConstructor
public class CreditoController {

    private final CreditoUseCasesImpl useCases;

    @GetMapping("/{numeroNfse}")
    public ResponseEntity<AppResponse<List<CreditoResponse>>> buscarPorNfse(@NotBlank @PathVariable String numeroNfse) {
        List<CreditoResponse> creditos = useCases.getCreditosByNfse(numeroNfse);

        if (creditos.isEmpty()) {
            return AppResponse.<List<CreditoResponse>>noContent("Nenhum crédito encontrado").toResponseEntity();
        }

        return AppResponse.ok("Créditos encontrados", creditos).toResponseEntity();
    }

    @GetMapping("/numero/{numeroCredito}")
    public ResponseEntity<AppResponse<CreditoResponse>> buscarPorNumeroCredito(@PathVariable String numeroCredito) {
        CreditoResponse credito = useCases.getCreditoByNumero(numeroCredito);
        return AppResponse.ok("Crédito encontrado", credito).toResponseEntity();
    }

}
