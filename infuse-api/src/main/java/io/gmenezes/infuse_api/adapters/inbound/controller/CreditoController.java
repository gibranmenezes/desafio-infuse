package io.gmenezes.infuse_api.adapters.inbound.controller;

import io.gmenezes.infuse_api.adapters.dtos.AppResponse;
import io.gmenezes.infuse_api.application.usecases.CreditoUseCases;
import io.gmenezes.infuse_api.domain.credito.dtos.CreditoResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/creditos")
@RequiredArgsConstructor
public class CreditoController {

    private final CreditoUseCases useCases;

    @GetMapping("/{numeroNfse}")
    public ResponseEntity<AppResponse<List<CreditoResponse>>> buscarPorNfse(
            @PathVariable @NotBlank(message = "O número da NFSe é obrigatório")  String numeroNfse) {
        List<CreditoResponse> creditos = useCases.getCreditosByNfse(numeroNfse);

        if (creditos.isEmpty()) {
            return AppResponse.<List<CreditoResponse>>noContent("Nenhum crédito encontrado").toResponseEntity();
        }

        return AppResponse.ok("Créditos encontrados", creditos).toResponseEntity();
    }

    @GetMapping("/credito/{numeroCredito}")
    public ResponseEntity<AppResponse<CreditoResponse>> buscarPorNumeroCredito(
            @PathVariable @NotBlank(message = "O número do crédito é obrigatório") String numeroCredito) {
        CreditoResponse credito = useCases.getCreditoByNumero(numeroCredito);
        return AppResponse.ok("Crédito encontrado", credito).toResponseEntity();
    }

}
