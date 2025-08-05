package pl.myc22ka.mathapp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.security.SecurityRequirement;

@Configuration
@RequiredArgsConstructor
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Moje Bagno")
                        .version("1.0.0")
                        .description("API documentation for testing backend application"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components());
    }
}
