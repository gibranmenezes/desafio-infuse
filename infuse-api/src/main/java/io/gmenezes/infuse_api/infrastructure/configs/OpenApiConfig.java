package io.gmenezes.infuse_api.infrastructure.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Infuse API - Consulta de Créditos")
                        .description("API para consulta de créditos associados a NFSe e número de crédito.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Gibran Menezes")
                                .email("gibranquimica@gmail.com")
                        )
                );
    }
}
