package io.gmenezes.infuse_api.adapters.inbound.controller;

import io.gmenezes.infuse_api.adapters.dtos.AppResponse;
import io.gmenezes.infuse_api.application.usecases.CreditoUseCases;
import io.gmenezes.infuse_api.domain.credito.dtos.CreditoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/creditos")
@RequiredArgsConstructor
@Tag(name = "Api de consulta de Créditos.")
public class CreditoController {

    private final CreditoUseCases useCases;

    @GetMapping(value = "/{numeroNfse}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Busca créditos por número da NFSe",description = "Retorna uma lista de créditos associados ao número da NFSe informada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de créditos encontrada com sucesso", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "204", description = "Nenhum crédito encontrado para o número da NFSe informado"),
            @ApiResponse(responseCode = "503", description = "Serviço indisponível")
    })
    @Parameter(in = ParameterIn.PATH, name = "numeroNfse", description = "Número da NFSe para buscar os créditos", required = true)
    public ResponseEntity<AppResponse<List<CreditoResponse>>> buscarPorNfse(
            @PathVariable @NotBlank(message = "O número da NFSe é obrigatório")  String numeroNfse) {
        List<CreditoResponse> creditos = useCases.getCreditosByNfse(numeroNfse);

        if (creditos.isEmpty()) {
            return AppResponse.<List<CreditoResponse>>noContent("Nenhum crédito encontrado").toResponseEntity();
        }

        return AppResponse.ok("Créditos encontrados", creditos).toResponseEntity();
    }

    @GetMapping("/credito/{numeroCredito}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Crédito encontrado para o número de crédito informado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Nenhum crédito encontrado para o número da número de crédito informado"),
            @ApiResponse(responseCode = "503", description = "Serviço indisponível")
    })
    @Parameter(in = ParameterIn.PATH, name = "numeroCredito", description = "Número do crédito a ser encontrado", required = true)
    public ResponseEntity<AppResponse<CreditoResponse>> buscarPorNumeroCredito(
            @PathVariable @NotBlank(message = "O número do crédito é obrigatório") String numeroCredito) {
        CreditoResponse credito = useCases.getCreditoByNumero(numeroCredito);
        return AppResponse.ok("Crédito encontrado", credito).toResponseEntity();
    }

}
