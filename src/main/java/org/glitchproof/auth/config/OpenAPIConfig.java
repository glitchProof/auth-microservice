package org.glitchproof.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Configuration;

@Configuration
@Profile("!prod")
public class OpenAPIConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Auth")
                .version("0.0.1")
                .description("Auth api for glitchProof");


        return new OpenAPI()
                .info(info);
    }
}
