package com.schooldevops.springboot.mongodbsamples

import com.schooldevops.springboot.mongodbsamples.model.Project
import com.schooldevops.springboot.mongodbsamples.model.Task
import com.schooldevops.springboot.mongodbsamples.service.ProjectService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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

    @GetMapping("/projects")
    fun findProject(@RequestParam id: String): ResponseEntity<Project> {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.findProject(id))
    }

    @GetMapping("/tasks")
    fun findTask(@RequestParam id: String): ResponseEntity<Task> {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.findTask(id))
    }

    @DeleteMapping("/tasks/{id}")
    fun deleteTask(@PathVariable id: String): ResponseEntity<String> {
        projectService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.OK).body(id)
    }

    @DeleteMapping("/projects/{id}")
    fun deleteProject(@PathVariable id: String): ResponseEntity<String> {
        projectService.deleteProject(id);
        return ResponseEntity.status(HttpStatus.OK).body(id)
    }
}