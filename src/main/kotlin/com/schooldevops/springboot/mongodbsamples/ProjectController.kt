package com.schooldevops.springboot.mongodbsamples

import com.schooldevops.springboot.mongodbsamples.model.Project
import com.schooldevops.springboot.mongodbsamples.model.ResultByStartDateAndCost
import com.schooldevops.springboot.mongodbsamples.model.ResultProjectTasks
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

    @GetMapping("/projects/findProjectByNameQueryWithTemplate")
    fun findProjectByNameQueryWithTemplate(@RequestParam name: String): ResponseEntity<List<Project>> {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.findProjectByNameQueryWithTemplate(name))
    }

    @GetMapping("/projects/findByEstimatedCostBetweenQueryWithTemplate")
    fun findByEstimatedCostBetweenQueryWithTemplate(@RequestParam from: Long, @RequestParam to: Long): ResponseEntity<List<Project>> {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.findByEstimatedCostBetweenQueryWithTemplate(from, to))
    }

    @GetMapping("/projects/findByNameRegexQueryWithTemplate")
    fun findByNameRegexQueryWithTemplate(@RequestParam name: String): ResponseEntity<List<Project>> {
        var regex = "^$name"
        return ResponseEntity.ok(projectService.findByNameRegexQueryWithTemplate(regex))
    }

    @GetMapping("/projects/upsertCostWithCriteriaTemplate/{id}/{cost}")
    fun upsertCostWithCriteriaTemplate(@PathVariable id: String, @PathVariable cost: Long) {
        projectService.upsertCostWithCriteriaTemplate(id, cost)
    }

    @GetMapping("/projects/deleteWithCriteriaTemplate/{id}")
    fun deleteWithCriteriaTemplate(@PathVariable id: String) {
        projectService.deleteWithCriteriaTemplate(id)
    }

    @GetMapping("/projects/findNoOfProjectsCostGreaterThan")
    fun findNoOfProjectsCostGreaterThan(@RequestParam cost: Long): ResponseEntity<Long> {
        return ResponseEntity.ok(projectService.findNoOfProjectsCostGreaterThan(cost))
    }

    @GetMapping("/projects/findCostsGroupByStartDateForProjectsCostGreaterThan")
    fun findCostsGroupByStartDateForProjectsCostGreaterThan(@RequestParam cost: Long): ResponseEntity<List<ResultByStartDateAndCost>> {
        return ResponseEntity.ok(projectService.findCostsGroupByStartDateForProjectsCostGreaterThan(cost))
    }

    @GetMapping("/projects/findAllProjectTasks")
    fun findAllProjectTasks(): ResponseEntity<List<ResultProjectTasks>> {
        return ResponseEntity.ok(projectService.findAllProjectTasks())
    }

    @GetMapping("/projects/findNameDescriptionForMatchingTerm")
    fun findNameDescriptionForMatchingTerm(@RequestParam term: String): ResponseEntity<List<Project>> {
        return ResponseEntity.ok(projectService.findNameDescriptionForMatchingTerm(term))
    }

    @GetMapping("/projects/findNameDescriptionForMatchingAny")
    fun findNameDescriptionForMatchingAny(@RequestParam words: List<String>): ResponseEntity<List<Project>> {
        val arr = words.toTypedArray()
        return ResponseEntity.ok(projectService.findNameDescriptionForMatchingAny(*arr))
    }

    @GetMapping("/projects/findNameDescriptionForMatchingPhrase")
    fun findNameDescriptionForMatchingPhrase(@RequestParam pharase: String): ResponseEntity<List<Project>>{
        return ResponseEntity.ok(projectService.findNameDescriptionForMatchingPhrase(pharase))
    }

    @PostMapping("/projects/saveProjectAndTask")
    fun saveProjectAndTask(): Unit {
        var list = listOf("UK", "India");
        val project = Project(
            id = "999999",
            code = "D",
            countryList = list,
            description = "ProjectDDecription",
            startDate = "2020-01-01",
            endDate = "2021-01-01",
            estimatedCost = 5000,
            name = "ProjectD",
            score = 50.0f
        )

        val task = Task(
            id = "999999",
            cost = 3000,
            description = "TaskDescription",
            name = "TaskK",
            ownername = "Tom",
            projectId = "999999"
        )

        projectService.saveProjectAndTask(project, task)
    }

}