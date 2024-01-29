package com.schooldevops.springboot.mongodbsamples

import com.schooldevops.springboot.mongodbsamples.model.Project
import com.schooldevops.springboot.mongodbsamples.model.Task
import com.schooldevops.springboot.mongodbsamples.service.ProjectService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class ProjectController(
    @Autowired val projectService: ProjectService
) {
    @PostMapping("/projects")
    fun saveProject(@RequestBody p: Project) : String{
        projectService.saveProject(p)
        return "OK"
    }

    @PostMapping("/tasks")
    fun saveTask(@RequestBody t: Task): String {
        projectService.saveTask(t)
        return "OK"
    }
}