package com.somnal.app.withpark.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.ForwardedHeaderFilter

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI =
        OpenAPI()
            .info(
                Info()
                    .title("Withpark API")
                    .description("Withpark Server API docs")
                    .version("1.0.0"),
            ).servers(
                listOf(
                    io.swagger.v3.oas.models.servers
                        .Server()
                        .url("http://localhost:8080"),
                ),
            )

    @Bean
    fun userApi(): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group("API for Withpark Client")
            .pathsToMatch("/api/**")
            .build()

    @Bean
    fun forwardedHeaderFilter(): ForwardedHeaderFilter = ForwardedHeaderFilter()
}
