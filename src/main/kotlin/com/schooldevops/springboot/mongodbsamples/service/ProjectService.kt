package com.schooldevops.springboot.mongodbsamples.service

import com.schooldevops.springboot.mongodbsamples.model.Project
import com.schooldevops.springboot.mongodbsamples.model.ResultByStartDateAndCost
import com.schooldevops.springboot.mongodbsamples.model.ResultProjectTasks
import com.schooldevops.springboot.mongodbsamples.model.Task
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.repository.Query

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


    fun findProjectByNameQuery(name: String): List<Project>
    fun findProjectNameAndCostQuery(name: String, cost: Long): List<Project>
    fun findByEstimatedCostBetweenQuery(from: Long, to: Long): List<Project>
    fun findByNameRegexQuery(regex: String): List<Project>

    fun findProjectByNameQueryWithTemplate(name: String): List<Project>
    fun findByEstimatedCostBetweenQueryWithTemplate(from: Long, to: Long): List<Project>
    fun findByNameRegexQueryWithTemplate(regexp: String): List<Project>
    fun upsertCostWithCriteriaTemplate(id: String, cost: Long)
    fun deleteWithCriteriaTemplate(id: String)

    fun findNoOfProjectsCostGreaterThan(cost: Long): Long
    fun findCostsGroupByStartDateForProjectsCostGreaterThan(cost: Long): List<ResultByStartDateAndCost>
    fun findAllProjectTasks(): List<ResultProjectTasks>

    fun findNameDescriptionForMatchingTerm(term: String): List<Project>
    fun findNameDescriptionForMatchingAny(vararg words: String): List<Project>
    fun findNameDescriptionForMatchingPhrase(phrase: String): List<Project>

    // Transactional
    fun saveProjectAndTask(p: Project, t: Task)
}