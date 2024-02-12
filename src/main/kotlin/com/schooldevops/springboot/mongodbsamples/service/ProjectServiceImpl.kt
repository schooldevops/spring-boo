package com.schooldevops.springboot.mongodbsamples.service

import com.schooldevops.springboot.mongodbsamples.model.Project
import com.schooldevops.springboot.mongodbsamples.model.Task
import com.schooldevops.springboot.mongodbsamples.repository.ProjectRepository
import com.schooldevops.springboot.mongodbsamples.repository.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class ProjectServiceImpl(
    @Autowired val projectRepository: ProjectRepository,
    @Autowired val taskRepository: TaskRepository
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
}