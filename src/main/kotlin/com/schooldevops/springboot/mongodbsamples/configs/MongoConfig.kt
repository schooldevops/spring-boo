package com.schooldevops.springboot.mongodbsamples.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.MongoTransactionManager

@Configuration
class MongoConfig {

    @Bean
    fun transactionManager(dbFactory: MongoDatabaseFactory) : MongoTransactionManager{
        return MongoTransactionManager(dbFactory)
    }
}