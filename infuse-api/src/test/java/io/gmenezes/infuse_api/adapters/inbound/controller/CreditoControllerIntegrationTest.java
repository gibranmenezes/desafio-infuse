package io.gmenezes.infuse_api.adapters.inbound.controller;

import io.gmenezes.infuse_api.application.usecases.CreditoUseCases;
import io.gmenezes.infuse_api.domain.credito.Credito;
import io.gmenezes.infuse_api.domain.credito.dtos.CreditoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CreditoController.class)
class CreditoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private CreditoUseCases creditoUseCases;

    private CreditoController creditoController;

    private CreditoResponse creditoResponse;
    private List<CreditoResponse> creditoResponseList;

    @BeforeEach
    void setUp() {
        creditoUseCases = mock(CreditoUseCases.class);
        creditoController = new CreditoController(creditoUseCases);

        creditoResponse = new CreditoResponse(
                "CR123456",
                "NFSE7891011",
                "2024-06-10",
                BigDecimal.valueOf(1500.75),
                "ISSQN",
                "Sim",
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(30000.00),
                BigDecimal.valueOf(5000.00),
                BigDecimal.valueOf(25000.00)
        );

        creditoResponseList = List.of(creditoResponse);
    }

    @Test
    void buscarPorNfse_deveRetornarListaDeCreditos_quandoExistiremCreditosParaNfse() throws Exception {
        String numeroNfse = "NFSE7891011";
        when(creditoUseCases.getCreditosByNfse(numeroNfse)).thenReturn(creditoResponseList);

        mockMvc.perform(get("/api/creditos/{numeroNfse}", numeroNfse)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Créditos encontrados")))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].numeroCredito", is("CR123456")))
                .andExpect(jsonPath("$.data[0].numeroNfse", is("NFSE7891011")))
                .andExpect(jsonPath("$.data[0].valorCredito", is(1500.75)));
    }

    @Test
    void buscarPorNfse_deveRetornarNoContent_quandoNaoExistiremCreditosParaNfse() throws Exception {
        String numeroNfse = "NFSE000000";
        when(creditoUseCases.getCreditosByNfse(numeroNfse)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/creditos/{numeroNfse}", numeroNfse)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Nenhum crédito encontrado")))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void buscarPorNumeroCredito_deveRetornarCreditoEncontrado_quandoExistirCreditoComNumero() throws Exception {
        String numeroCredito = "CR123456";
        when(creditoUseCases.getCreditoByNumero(numeroCredito)).thenReturn(creditoResponse);

        mockMvc.perform(get("/api/creditos/credito/{numeroCredito}", numeroCredito)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Crédito encontrado")))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.numeroCredito", is("CR123456")))
                .andExpect(jsonPath("$.data.numeroNfse", is("NFSE7891011")))
                .andExpect(jsonPath("$.data.dataEmissao", is("2024-06-10")))
                .andExpect(jsonPath("$.data.tipoCredito", is("ISSQN")))
                .andExpect(jsonPath("$.data.valorCredito", is(1500.75)))
                .andExpect(jsonPath("$.data.percentualMaximo", is(5.0)));
    }

    @Test
    void buscarPorNumeroCredito_deveLancarExcecao_quandoNaoExistirCreditoComNumero() throws Exception {
        String numeroCredito = "CREDITO_INEXISTENTE";
        when(creditoUseCases.getCreditoByNumero(numeroCredito))
                .thenThrow(new org.hibernate.ObjectNotFoundException("Crédito não encontrado para o numero: " + numeroCredito, Credito.class));

        mockMvc.perform(get("/api/creditos/credito/{numeroCredito}", numeroCredito)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void buscarPorNfse_deveLancarBadRequest_quandoNfseForInvalida() throws Exception {
        mockMvc.perform(get("/api/creditos/ ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarPorNumeroCredito_deveLancarBadRequest_quandoNumeroCreditoForInvalido() throws Exception {
        mockMvc.perform(get("/api/creditos/credito/ ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}