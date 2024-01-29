package com.schooldevops.springboot.mongodbsamples

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MongodbSamplesApplication

fun main(args: Array<String>) {
	runApplication<MongodbSamplesApplication>(*args)
}
