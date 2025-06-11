package io.gmenezes.infuse_api.usecases;

import io.gmenezes.infuse_api.application.services.CreditoUseCasesImpl;
import io.gmenezes.infuse_api.domain.credito.Credito;
import io.gmenezes.infuse_api.domain.credito.CreditoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CreditoUseCasesImplTest {

    @Mock
    private CreditoRepository repository;

    @InjectMocks
    private CreditoUseCasesImpl service;

    @Test
    void getCreditosByNfse_deve_retornar_lista_de_creditos(){

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
}
