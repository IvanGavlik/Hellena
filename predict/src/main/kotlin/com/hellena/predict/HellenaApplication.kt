package com.hellena.predict

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication(scanBasePackages = ["com.hellena.predict.*"])
class HellenaApplication

fun main(args: Array<String>) {
	runApplication<HellenaApplication>(*args)
}


@Configuration
class WebServerConfiguration {

	@Value("\${cors.originPatterns:default}")
	private val corsOriginPatterns: String = "*"

	@Bean
	fun addCorsConfig(): WebMvcConfigurer {
		return object : WebMvcConfigurer {
			override fun addCorsMappings(registry: CorsRegistry) {
				val allowedOrigins = corsOriginPatterns.split(",").toTypedArray()
				registry.addMapping("/**")
					.allowedMethods("*")
					.allowedOriginPatterns(*allowedOrigins)
					.allowCredentials(true)
			}
		}
	}

}
