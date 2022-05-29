package com.kep.schoolmeal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class SchoolMealApplication

fun main(args: Array<String>) {
	runApplication<SchoolMealApplication>(*args)
}
