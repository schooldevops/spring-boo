package com.schooldevops.springboot.mongodbsamples.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Field

class Project (
    @Id var id: String,
    var name: String,
    var code: String?,
    @Field("desc") var description: String?,
    var startDate: String?,
    var endDate: String?,
    @Field("cost") var estimatedCost: Long,
    var countryList: List<String>?
) {
    // optimistic lock을 위해 버변을 걸어준다.
    @Version var version: Long = 0
}