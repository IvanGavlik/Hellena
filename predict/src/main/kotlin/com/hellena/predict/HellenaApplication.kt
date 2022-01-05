package com.hellena.predict

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.hellena.predict.*"])
class HellenaApplication

fun main(args: Array<String>) {
	runApplication<HellenaApplication>(*args)
}



