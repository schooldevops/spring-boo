package com.schooldevops.springboot.mongodbsamples.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.mongodb.BasicDBObject
import com.mongodb.client.gridfs.model.GridFSFile
import com.schooldevops.springboot.mongodbsamples.model.*
import com.schooldevops.springboot.mongodbsamples.repository.ProjectRepository
import com.schooldevops.springboot.mongodbsamples.repository.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.mongo.MongoConnectionDetails.GridFs
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.LookupOperation
import org.springframework.data.mongodb.core.query.*
import org.springframework.data.mongodb.gridfs.GridFsOperations
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

@Service
class ProjectServiceImpl(
    @Autowired val projectRepository: ProjectRepository,
    @Autowired val taskRepository: TaskRepository,
    @Autowired val mongoTemplate: MongoTemplate,
    @Autowired val gridFsTemplate: GridFsTemplate,
    @Autowired val gridFsOperations: GridFsOperations
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

    override fun findNameDescriptionForMatchingTerm(term: String): List<Project> {
        val query = TextQuery.queryText(TextCriteria.forDefaultLanguage().matching(term))
            .sortByScore().with(Sort.by(Sort.Direction.DESC, "score"))
        return mongoTemplate.find(query, Project::class.java)
    }

    override fun findNameDescriptionForMatchingAny(vararg words: String): List<Project> {
        val query = TextQuery.queryText(TextCriteria.forDefaultLanguage().matchingAny(*words))
            .sortByScore().with(Sort.by(Sort.Direction.DESC, "score"))
        return mongoTemplate.find(query, Project::class.java)
    }

    override fun findNameDescriptionForMatchingPhrase(phrase: String): List<Project> {
        val query = TextQuery.queryText(TextCriteria.forDefaultLanguage().matchingPhrase(phrase))
            .sortByScore().with(Sort.by(Sort.Direction.DESC, "score"))
        return mongoTemplate.find(query, Project::class.java)
    }

    @Transactional
    override fun saveProjectAndTask(p: Project, t: Task) {
        taskRepository.save(t)
        projectRepository.save(p)
    }

    override fun chunkAndSaveProject(p: Project) {
        var s = serializetoJson(p)
        val byteArrayInputStream = ByteArrayInputStream(s.toByteArray())
        val basicDBObject = BasicDBObject()
        basicDBObject.put("projectId", p.id)

        gridFsTemplate.store(byteArrayInputStream, p.id, basicDBObject)
    }

    private fun serializetoJson(p: Project): String {
        val objectMapper = ObjectMapper()
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(p)
    }

    private fun deserialize(json: String): Project {
        val objectMapper = ObjectMapper().registerModule(
            KotlinModule.Builder()
                .withReflectionCacheSize(512)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, false)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build()
        )

        return objectMapper.readValue(json)
    }

    override fun loadProjectFromGrid(projectId: String): Project {
        val file = gridFsTemplate.findOne(
            Query(
                Criteria.where("metadata.projectId").`is`(projectId)
            ).with(Sort.by(Sort.Direction.DESC, "uploadDate")).limit(1))

        val inputStream = gridFsOperations.getResource(file).getInputStream()
        val stmlXml = BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(
            Collectors.joining("\n"))

        val p = deserialize(stmlXml)

        return p
    }

    override fun deleteProjectFromGrid(projectId: String) {
        gridFsTemplate.delete(Query(Criteria.where("metadata.projectId").`is`(projectId)))
    }
}