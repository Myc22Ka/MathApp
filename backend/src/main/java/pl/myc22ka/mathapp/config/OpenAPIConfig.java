package pl.myc22ka.mathapp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration for Swagger UI.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 17.10.2025
 */

@Configuration
@RequiredArgsConstructor
public class OpenAPIConfig {

    /**
     * Creates custom OpenAPI bean for Swagger UI.
     *
     * @return configured OpenAPI instance
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MathApp Backend API")
                        .version("1.2.0")
                        .description("API documentation for testing backend application"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components());
    }
}
