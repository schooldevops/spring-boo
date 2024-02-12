package com.schooldevops.springboot.mongodbsamples.repository

import com.schooldevops.springboot.mongodbsamples.model.Project
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

/**
 * Query keyworkds: https://docs.spring.io/spring-data/mongodb/reference/repositories/query-keywords-reference.html
 */
interface ProjectRepository: MongoRepository<Project, String> {
    /**
     * {"name": name}
     */
    fun findByName(name: String): List<Project>
    /**
     * {"name": {"$ne": name}}
     */
    fun findByNameNot(name: String): List<Project>
    /**
     * {"cost": {"$gt": cost}}
     */
    fun findByEstimatedCostGreaterThan(cost: Long): List<Project>
    /**
     * {"cost": {"$gt": from, "&lt": to}}
     */
    fun findByEstimatedCostBetween(from: Long, to: Long): List<Project>
    /**
     * {"name": /name/}
     */
    fun findByNameLike(name: String): List<Project>
    /**
     * {"name": {"$regex": name}}
     */
    fun findByNameRegex(name: String): List<Project>

    @Query("{'name': ?0}")
    fun findProjectByNameQuery(name: String): List<Project>

    @Query("{'name':  ?0, 'cost': ?1}")
    fun findProjectNameAndCostQuery(name: String, cost: Long): List<Project>

    @Query("{'cost':  {\$lt:  ?1, \$gt:  ?0}}")
    fun findByEstimatedCostBetweenQuery(from: Long, to: Long, sort: Sort): List<Project>

    @Query(value = "{'name':  {\$regex:  ?0} }", fields = "{'name':  1, 'cost':  1}" )
    fun findByNameRegexQuery(regex: String): List<Project>
}