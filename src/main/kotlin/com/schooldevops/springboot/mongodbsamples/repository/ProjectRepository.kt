package com.schooldevops.springboot.mongodbsamples.repository

import com.schooldevops.springboot.mongodbsamples.model.Project
import org.springframework.data.mongodb.repository.MongoRepository

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
    fun findByEstimatedCostBetween(from: Long, to: Long)
    /**
     * {"name": /name/}
     */
    fun findByNameLike(name: String): List<Project>
    /**
     * {"name": {"$regex": name}}
     */
    fun findByNameRegex(name: String): List<Project>
}