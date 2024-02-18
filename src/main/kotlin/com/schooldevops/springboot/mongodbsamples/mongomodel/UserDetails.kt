package com.schooldevops.springboot.mongodbsamples.mongomodel

import org.springframework.data.mongodb.core.mapping.Document

class UserDetails (
    var gender: String,
    var height: Int
){
}