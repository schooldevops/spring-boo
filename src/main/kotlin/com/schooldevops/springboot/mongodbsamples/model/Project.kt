package com.schooldevops.springboot.mongodbsamples.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.IndexDirection
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Field


@CompoundIndex(name = "namecostindex", def="{'name': 1, 'cost': 1}")
class Project (
    @Id var id: String,
    @Indexed(name = "nameindex", direction= IndexDirection.ASCENDING)
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