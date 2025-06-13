package io.gmenezes.infuse_api.application.usecases;

public interface EventUseCase {

    void registrarConsultaNfse(String numeroNfse, boolean sucesso, Object detalhes);
    void registrarConsultaCredito(String numeroCredito, boolean sucesso, Object detalhes);

}
