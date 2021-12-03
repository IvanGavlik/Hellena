package com.hellena.predict.api

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.*


@Configuration
class Configuration {

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource? {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = Arrays.asList("*")
        configuration.allowedMethods = Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
        configuration.allowedHeaders = Arrays.asList(
            "Access-Control-Allow-Headers", "Access-Control-Allow-Origin",
            "Access-Control-Request-Method", "Access-Control-Request-Headers", "Origin",
            "Cache-Control", "Content-Type"
        )
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

}
