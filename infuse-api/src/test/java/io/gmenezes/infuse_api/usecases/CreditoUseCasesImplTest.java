package io.gmenezes.infuse_api.usecases;

import io.gmenezes.infuse_api.application.services.CreditoUseCasesImpl;
import io.gmenezes.infuse_api.application.usecases.EventUseCase;
import io.gmenezes.infuse_api.domain.credito.Credito;
import io.gmenezes.infuse_api.domain.credito.CreditoRepository;
import static org.junit.jupiter.api.Assertions.*;

import io.gmenezes.infuse_api.domain.credito.dtos.CreditoResponse;
import io.gmenezes.infuse_api.util.CreditoMapper;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.AssertionsKt.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreditoUseCasesImplTest {

    @Mock
    private CreditoRepository repository;

    @Mock
    private CreditoMapper mapper;

    @Mock
    private EventUseCase eventService;

    @InjectMocks
    private CreditoUseCasesImpl service;

    @Test
    void getCreditosByNfse_deve_retornar_lista_de_creditos(){
        var creditoList = getCreditoList();
        var responseList = getResponseList();
        String nfse = "7891011";

        when(repository.findAllByNfse(nfse)).thenReturn(creditoList);
        when(mapper.fromCreditoToResponse(creditoList.getFirst())).thenReturn(responseList.getFirst());

        var result = service.getCreditosByNfse(nfse);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(creditoList.getFirst().getNumeroCredito(), result.getFirst().numeroCredito());


        verify(eventService).registrarConsultaNfse(
               any(String.class),
                any(Boolean.class),
                any(Map.class)
        );

    }

    @Test
    void getCreditoByNumero_deve_retonar_credito() {
        String numeroCredito = "123456";
        Credito credito = getCreditoList().getFirst();
        CreditoResponse response = getResponseList().getFirst();

        when(repository.findByNumeroCredito(numeroCredito)).thenReturn(credito);
        when(mapper.fromCreditoToResponse(credito)).thenReturn(response);

        CreditoResponse result = service.getCreditoByNumero(numeroCredito);

        assertNotNull(result);
        assertEquals(credito.getNumeroCredito(), result.numeroCredito());

        verify(eventService).registrarConsultaCredito(
                any(String.class),
                any(Boolean.class),
                any(Map.class)
        );
    }


    @Test
    void getCreditoByNumero_deve_lancar_ObjectNotFoundException_quando_nao_encontrar_credito() {
        String numeroCredito = "9999";
        when(repository.findByNumeroCredito(numeroCredito)).thenReturn(null);

        assertThrows(
                ObjectNotFoundException.class,
                () -> service.getCreditoByNumero(numeroCredito),
                "Crédito não encontrado para o numero: " + numeroCredito
        );

        verify(eventService).registrarConsultaCredito(
                any(String.class),
                any(Boolean.class),
                any(Map.class)
        );


    }

    private List<Credito> getCreditoList() {
        return List.of(
                new Credito("123456",
                        "7891011",
                        LocalDate.of(2024, 2,25),
                        BigDecimal.valueOf(1500.75),  "ISSQN", true,
                        BigDecimal.valueOf(5.0),
                        BigDecimal.valueOf(30000.00),
                        BigDecimal.valueOf(5000.00),
                        BigDecimal.valueOf(25000.00)
                ));
    }

    private List<CreditoResponse> getResponseList() {
        return List.of(
                new CreditoResponse("123456",
                        "7891011",
                        "2024-02-25",
                        BigDecimal.valueOf(1500.75),  "ISSQN", "Sim",
                        BigDecimal.valueOf(5.0),
                        BigDecimal.valueOf(30000.00),
                        BigDecimal.valueOf(5000.00),
                        BigDecimal.valueOf(25000.00)
                ));
    }
}
