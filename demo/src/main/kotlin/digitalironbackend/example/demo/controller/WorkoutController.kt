package digitalironbackend.example.demo.controller

import digitalironbackend.example.demo.dto.*
import digitalironbackend.example.demo.service.WorkoutService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/workouts")
class WorkoutController(private val workoutService: WorkoutService) {

    // --- Workouts ---

    @GetMapping
    fun getAll(@AuthenticationPrincipal userId: String): ResponseEntity<List<WorkoutResponse>> =
        ResponseEntity.ok(workoutService.getAllForUser(userId))

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): ResponseEntity<WorkoutResponse> =
        ResponseEntity.ok(workoutService.getById(id))

    @PostMapping
    fun create(
        @AuthenticationPrincipal userId: String,
        @RequestBody request: WorkoutRequest
    ): ResponseEntity<WorkoutResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(workoutService.create(userId, request))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: String,
        @RequestBody request: WorkoutRequest
    ): ResponseEntity<WorkoutResponse> =
        ResponseEntity.ok(workoutService.update(id, request))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Void> {
        workoutService.delete(id)
        return ResponseEntity.noContent().build()
    }

    // --- Workout Sets ---

    @PostMapping("/{workoutId}/exercises/{exerciseId}/sets")
    fun addSet(
        @PathVariable workoutId: String,
        @PathVariable exerciseId: String,
        @RequestBody request: WorkoutSetRequest
    ): ResponseEntity<WorkoutSetResponse> =
        ResponseEntity.status(HttpStatus.CREATED)
            .body(workoutService.addSet(workoutId, exerciseId, request))

    @PutMapping("/{workoutId}/exercises/{exerciseId}/sets/{setId}")
    fun updateSet(
        @PathVariable workoutId: String,
        @PathVariable exerciseId: String,
        @PathVariable setId: String,
        @RequestBody request: WorkoutSetRequest
    ): ResponseEntity<WorkoutSetResponse> =
        ResponseEntity.ok(workoutService.updateSet(workoutId, exerciseId, setId, request))

    @DeleteMapping("/{workoutId}/exercises/{exerciseId}/sets/{setId}")
    fun deleteSet(
        @PathVariable workoutId: String,
        @PathVariable exerciseId: String,
        @PathVariable setId: String
    ): ResponseEntity<Void> {
        workoutService.deleteSet(workoutId, exerciseId, setId)
        return ResponseEntity.noContent().build()
    }
}