package com.schooldevops.springboot.mongodbsamples

import com.schooldevops.springboot.mongodbsamples.model.Project
import com.schooldevops.springboot.mongodbsamples.model.Task
import com.schooldevops.springboot.mongodbsamples.service.ProjectService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    @PutMapping("/projects")
    fun updateProject(@RequestBody p: Project) : Unit {
        projectService.updateProject(p)
    }

    @GetMapping("/projects/findByName")
    fun findByName(@RequestParam name: String): ResponseEntity<List<Project>> {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.findByName(name))
    }

    @GetMapping("/projects/findByNameNot")
    fun findByNameNot(@RequestParam name: String): ResponseEntity<List<Project>> {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.findByNameNot(name))
    }

    @GetMapping("/projects/findByEstimatedCostGreaterThan")
    fun findByEstimatedCostGreaterThan(@RequestParam cost: Long): ResponseEntity<List<Project>> {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.findByEstimatedCostGreaterThan(cost))
    }

    @GetMapping("/projects/findByNameLike")
    fun findByNameLike(@RequestParam name: String): ResponseEntity<List<Project>> {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.findByNameLike(name))
    }

    @GetMapping("/projects/findByNameRegex")
    fun findByNameRegex(@RequestParam name: String): ResponseEntity<List<Project>> {
        var regEx = "^$name"
        return ResponseEntity.status(HttpStatus.OK).body(projectService.findByNameRegex(regEx))
    }

    @GetMapping("/projects/findProjectByNameQuery")
    fun findProjectByNameQuery(@RequestParam name: String): ResponseEntity<List<Project>> {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.findProjectByNameQuery(name))
    }

    @GetMapping("/projects/findProjectNameAndCostQuery")
    fun findProjectNameAndCostQuery(@RequestParam name: String, @RequestParam cost: Long): ResponseEntity<List<Project>> {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.findProjectNameAndCostQuery(name, cost))
    }

    @GetMapping("/projects/findByEstimatedCostBetweenQuery")
    fun findByEstimatedCostBetweenQuery(@RequestParam from: Long, @RequestParam to: Long): ResponseEntity<List<Project>> {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.findByEstimatedCostBetweenQuery(from, to))
    }

    @GetMapping("/projects/findByNameRegexQuery")
    fun findByNameRegexQuery(@RequestParam name: String): ResponseEntity<List<Project>> {
        var regEx = "^$name"
        return ResponseEntity.status(HttpStatus.OK).body(projectService.findByNameRegexQuery(regEx))
    }
}