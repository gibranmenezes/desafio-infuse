package io.gmenezes.infuse_api.usecases;

import io.gmenezes.infuse_api.adapters.dtos.ConsultaEvent;
import io.gmenezes.infuse_api.application.services.EvenUseCaseImpl;
import io.gmenezes.infuse_api.application.usecases.CreditoUseCases;
import io.gmenezes.infuse_api.domain.event.EventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvenUseCaseImplTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private CreditoUseCases creditoUseCases;

    @InjectMocks
    private EvenUseCaseImpl eventUseCase;

    @Captor
    private ArgumentCaptor<ConsultaEvent> eventCaptor;

    private Map<String, Object> detalhes;

    @BeforeEach
    void setUp() {
        detalhes = new HashMap<>();
        detalhes.put("chave", "valor");
    }

    @Test
    void registrarConsultaNfse_deve_publicar_evento_com_sucesso() {
        String numeroNfse = "123456789";
        boolean sucesso = true;

        eventUseCase.registrarConsultaNfse(numeroNfse, sucesso, detalhes);

        verify(eventPublisher, times(1)).publicarEvento(eventCaptor.capture());

        ConsultaEvent eventoCapturado = eventCaptor.getValue();
        assertNotNull(eventoCapturado);
        assertNotNull(eventoCapturado.getId());
        assertNotNull(eventoCapturado.getTimestamp());
        assertEquals("nfse", eventoCapturado.getTipoConsulta());
        assertEquals("sucesso", eventoCapturado.getResultado());
        assertEquals(detalhes, eventoCapturado.getDetalhes());
    }

    @Test
    void registrarConsultaNfse_deve_publicar_evento_com_erro() {
        String numeroNfse = "123456789";
        boolean sucesso = false;

        eventUseCase.registrarConsultaNfse(numeroNfse, sucesso, detalhes);

        verify(eventPublisher, times(1)).publicarEvento(eventCaptor.capture());

        ConsultaEvent eventoCapturado = eventCaptor.getValue();
        assertNotNull(eventoCapturado);
        assertEquals("erro", eventoCapturado.getResultado());
    }

    @Test
    void registrarConsultaNfse_deve_capturar_excecao_quando_publicar_falhar() {
        String numeroNfse = "123456789";
        doThrow(new RuntimeException("Erro ao publicar")).when(eventPublisher).publicarEvento(any());

        assertDoesNotThrow(() -> eventUseCase.registrarConsultaNfse(numeroNfse, true, detalhes));

        verify(eventPublisher, times(1)).publicarEvento(any());
    }

    @Test
    void registrarConsultaCredito_deve_publicar_evento_com_sucesso() {
        String numeroCredito = "CRED123";
        boolean sucesso = true;

        eventUseCase.registrarConsultaCredito(numeroCredito, sucesso, detalhes);

        verify(eventPublisher, times(1)).publicarEvento(eventCaptor.capture());

        ConsultaEvent eventoCapturado = eventCaptor.getValue();
        assertNotNull(eventoCapturado);
        assertNotNull(eventoCapturado.getId());
        assertNotNull(eventoCapturado.getTimestamp());
        assertEquals("credito", eventoCapturado.getTipoConsulta());
        assertEquals("sucesso", eventoCapturado.getResultado());
        assertEquals(detalhes, eventoCapturado.getDetalhes());
    }

    @Test
    void registrarConsultaCredito_deve_publicar_evento_com_erro() {
        String numeroCredito = "CRED123";
        boolean sucesso = false;

        eventUseCase.registrarConsultaCredito(numeroCredito, sucesso, detalhes);

        verify(eventPublisher, times(1)).publicarEvento(eventCaptor.capture());

        ConsultaEvent eventoCapturado = eventCaptor.getValue();
        assertNotNull(eventoCapturado);
        assertEquals("erro", eventoCapturado.getResultado());
    }

    @Test
    void registrarConsultaCredito_deve_capturar_excecao_quando_publicar_falhar() {
        String numeroCredito = "CRED123";
        doThrow(new RuntimeException("Erro ao publicar")).when(eventPublisher).publicarEvento(any());

        assertDoesNotThrow(() -> eventUseCase.registrarConsultaCredito(numeroCredito, false, detalhes));

        verify(eventPublisher, times(1)).publicarEvento(any());
    }
}