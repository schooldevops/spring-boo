package com.schooldevops.springboot.mongodbsamples.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Field

class Task(
    @Id var id: String,
    @Field("pid") var projectId: String,
    var name: String,
    @Field("desc") var description: String,
    var ownername: String,
    var cost: Long
) {
    @Version var version: Long = 0
}