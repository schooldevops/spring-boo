package com.schooldevops.springboot.mongodbsamples.service

import com.schooldevops.springboot.mongodbsamples.model.Project
import com.schooldevops.springboot.mongodbsamples.model.Task

interface ProjectService {
    fun saveProject(p: Project): Unit
    fun saveTask(t: Task): Unit
    fun findTask(id: String): Task?
    fun findProject(id: String): Project?
    fun deleteTask(id: String)
    fun deleteProject(id: String)
}