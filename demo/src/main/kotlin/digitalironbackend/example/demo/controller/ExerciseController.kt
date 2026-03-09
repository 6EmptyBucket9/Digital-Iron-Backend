package digitalironbackend.example.demo.controller

import digitalironbackend.example.demo.dto.ExerciseRequest
import digitalironbackend.example.demo.dto.ExerciseResponse
import digitalironbackend.example.demo.service.ExerciseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/exercises")
class ExerciseController(private val exerciseService: ExerciseService) {

    @GetMapping
    fun getAll(): ResponseEntity<List<ExerciseResponse>> =
        ResponseEntity.ok(exerciseService.getAll())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): ResponseEntity<ExerciseResponse> =
        ResponseEntity.ok(exerciseService.getById(id))

    @PostMapping
    fun create(@RequestBody request: ExerciseRequest): ResponseEntity<ExerciseResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(exerciseService.create(request))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: String,
        @RequestBody request: ExerciseRequest
    ): ResponseEntity<ExerciseResponse> =
        ResponseEntity.ok(exerciseService.update(id, request))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Void> {
        exerciseService.delete(id)
        return ResponseEntity.noContent().build()
    }
}