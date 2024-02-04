package com.schooldevops.springboot.mongodbsamples.service

import com.schooldevops.springboot.mongodbsamples.model.Project
import com.schooldevops.springboot.mongodbsamples.model.Task
import com.schooldevops.springboot.mongodbsamples.repository.ProjectRepository
import com.schooldevops.springboot.mongodbsamples.repository.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
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
        projectRepository.save(p);
    }
}