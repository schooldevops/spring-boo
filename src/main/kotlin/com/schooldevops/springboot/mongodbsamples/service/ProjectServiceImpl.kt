package com.schooldevops.springboot.mongodbsamples.service

import com.schooldevops.springboot.mongodbsamples.model.*
import com.schooldevops.springboot.mongodbsamples.repository.ProjectRepository
import com.schooldevops.springboot.mongodbsamples.repository.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.LookupOperation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service

@Service
class ProjectServiceImpl(
    @Autowired val projectRepository: ProjectRepository,
    @Autowired val taskRepository: TaskRepository,
    @Autowired val mongoTemplate: MongoTemplate
): ProjectService {

    override fun saveProject(p: Project) {
        projectRepository.save(p)
    }

    override fun saveTask(t: Task) {
        taskRepository.save(t)
    }

    override fun findTask(id: String): Task? {
        return taskRepository.findById(id).orElseThrow { NotFoundException() }
    }

    override fun findProject(id: String): Project? {
        return projectRepository.findById(id).orElseThrow() { NotFoundException() }
    }

    override fun deleteTask(id: String) {
        taskRepository.deleteById(id)
    }

    override fun deleteProject(id: String) {
        projectRepository.deleteById(id)
    }

    /**
     * version에 따라 optimistics lock이 걸린다.
     */
    override fun updateProject(p: Project) {
        projectRepository.save(p)
    }

    override fun findByName(name: String): List<Project> {
        return projectRepository.findByName(name)
    }

    override fun findByNameNot(name: String): List<Project> {
        return projectRepository.findByNameNot(name)
    }

    override fun findByEstimatedCostGreaterThan(cost: Long): List<Project> {
        return projectRepository.findByEstimatedCostGreaterThan(cost)
    }

    override fun findByEstimatedCostBetween(from: Long, to: Long): List<Project> {
        return projectRepository.findByEstimatedCostBetween(from, to)
    }

    override fun findByNameLike(name: String): List<Project> {
        return projectRepository.findByNameLike(name)
    }

    override fun findByNameRegex(name: String): List<Project> {
        return projectRepository.findByNameRegex(name)
    }

    override fun findProjectByNameQuery(name: String): List<Project> {
        return projectRepository.findProjectByNameQuery(name)
    }

    override fun findProjectNameAndCostQuery(name: String, cost: Long): List<Project> {
        return projectRepository.findProjectNameAndCostQuery(name, cost)
    }

    override fun findByEstimatedCostBetweenQuery(from: Long, to: Long): List<Project> {
        return projectRepository.findByEstimatedCostBetweenQuery(from, to, Sort.by(Sort.Direction.DESC, "cost"))
    }

    override fun findByNameRegexQuery(regex: String): List<Project> {
        return projectRepository.findByNameRegexQuery(regex)
    }

    override fun findProjectByNameQueryWithTemplate(name: String): List<Project> {
        var query = Query()
        query.addCriteria(Criteria.where("name").`is`(name))
        return mongoTemplate.find(query, Project::class.java)
    }

    override fun findByEstimatedCostBetweenQueryWithTemplate(from: Long, to: Long): List<Project> {
        val query = Query()
        query.with(Sort.by(Sort.Direction.ASC, "cost"))
        query.addCriteria(Criteria.where("cost").gt(from))
        return mongoTemplate.find(query, Project::class.java)
    }

    override fun findByNameRegexQueryWithTemplate(regexp: String): List<Project> {
        val query = Query()
        query.addCriteria(Criteria.where("name").regex(regexp))
        return mongoTemplate.find(query, Project::class.java)
    }

    override fun upsertCostWithCriteriaTemplate(id: String, cost: Long) {
        val query = Query()
        query.addCriteria(Criteria.where("id").`is`(id))
        val update = Update()
        update.set("cost", cost)
        mongoTemplate.upsert(query, update, Project::class.java)
    }

    override fun deleteWithCriteriaTemplate(id: String) {
        val query = Query()
        query.addCriteria(Criteria.where("id").`is`(id))
        mongoTemplate.remove(query, Project::class.java)
    }

    override fun findNoOfProjectsCostGreaterThan(cost: Long): Long {
        val match = Aggregation.match(Criteria("cost").gt(cost))
        val countOperation = Aggregation.count().`as`("costly_projects")

        val aggre = Aggregation.newAggregation(match, countOperation)
        val output = mongoTemplate.aggregate(aggre, "project", ResultCount::class.java)

        if (output.mappedResults.size > 0) {
            return output.mappedResults.get(0).costly_projects
        }

        return 0;

    }

    override fun findCostsGroupByStartDateForProjectsCostGreaterThan(cost: Long): List<ResultByStartDateAndCost> {
        val match = Aggregation.match(Criteria("cost").gt(cost))
        val groupOperation = Aggregation.group("startDate").sum("cost").`as`("total")
        val sort = Aggregation.sort(Sort.by(Sort.Direction.DESC, "total"))

        val aggregation = Aggregation.newAggregation(match, groupOperation, sort)
        val output = mongoTemplate.aggregate(aggregation, "project", ResultByStartDateAndCost::class.java)
        return output.mappedResults;
    }

    override fun findAllProjectTasks(): List<ResultProjectTasks> {
        val lookupOperation = LookupOperation.newLookup()
            .from("task")
            .localField("_id")
            .foreignField("pid")
            .`as`("ProjectTasks")
        val unwind = Aggregation.unwind("ProjectTasks")
        val projectionOperation = Aggregation.project().andExpression("_id").`as`("_id")
            .andExpression("name").`as`("name")
            .andExpression("ProjectTasks.name").`as`("taskName")
            .andExpression("ProjectTasks.ownername").`as`("taskOwnerName")
        val aggregation = Aggregation.newAggregation(lookupOperation, unwind, projectionOperation)
        return mongoTemplate.aggregate(aggregation, "project", ResultProjectTasks::class.java).mappedResults

    }
}