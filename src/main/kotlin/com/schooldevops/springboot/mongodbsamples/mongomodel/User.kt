package com.schooldevops.springboot.mongodbsamples.mongomodel

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collation = "user")
class User (
    @Id var id: String,
    var name: String,
    var age: Byte,
    var userDetails: UserDetails,
    @Field("pns") var phoneNumbers: List<String>,
    var emails: List<Email>,
    @Field("payinfo") var paymentInfo: Map<String, String>
){
}