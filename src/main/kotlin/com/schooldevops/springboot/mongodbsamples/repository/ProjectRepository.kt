package com.schooldevops.springboot.mongodbsamples.repository

import com.schooldevops.springboot.mongodbsamples.model.Project
import org.springframework.data.mongodb.repository.MongoRepository

interface ProjectRepository: MongoRepository<Project, String> {
}