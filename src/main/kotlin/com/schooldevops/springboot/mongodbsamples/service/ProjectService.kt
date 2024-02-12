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
    fun updateProject(p: Project)

    fun findByName(name: String): List<Project>
    fun findByNameNot(name: String): List<Project>
    fun findByEstimatedCostGreaterThan(cost: Long): List<Project>
    fun findByEstimatedCostBetween(from: Long, to: Long): List<Project>
    fun findByNameLike(name: String): List<Project>
    fun findByNameRegex(name: String): List<Project>
}