package com.schooldevops.springboot.mongodbsamples.repository

import com.schooldevops.springboot.mongodbsamples.model.Task
import org.springframework.data.mongodb.repository.MongoRepository

interface TaskRepository: MongoRepository<Task, String> {
}