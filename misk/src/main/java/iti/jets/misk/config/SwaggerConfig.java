package iti.jets.misk.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//http://localhost:8085/swagger-ui/index.html

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MISK APP APIs")
                        .version("1.0")
                        .description("This is  API documentation for  MISK application."));
    }

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/**")
//                .addOpenApiCustomizer(openApi -> {
//                    // Remove problematic schemas
//                    openApi.getComponents().getSchemas().remove("Sort");
//                    openApi.getComponents().getSchemas().remove("Pageable");
//                })
                .build();
    }

}
