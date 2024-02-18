package com.schooldevops.springboot.mongodbsamples.configs

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.gridfs.GridFsTemplate

@Configuration
class MongoConfig(
    var mongoConverter: MappingMongoConverter,
    var dbFactory: MongoDatabaseFactory
) {

    @Bean
    fun transactionManager(dbFactory: MongoDatabaseFactory) : MongoTransactionManager{
        return MongoTransactionManager(dbFactory)
    }

    @Bean
    @Throws(Exception::class)
    fun gridFsTemplate(): GridFsTemplate{
        return GridFsTemplate(dbFactory, mongoConverter)
    }
}