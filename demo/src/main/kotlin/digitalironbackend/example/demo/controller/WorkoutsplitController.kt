package digitalironbackend.example.demo.controller

import digitalironbackend.example.demo.dto.*
import digitalironbackend.example.demo.service.WorkoutSplitService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/splits")
class WorkoutSplitController(private val splitService: WorkoutSplitService) {

    // --- Splits ---

    @GetMapping
    fun getAll(@AuthenticationPrincipal userId: String): ResponseEntity<List<WorkoutSplitResponse>> =
        ResponseEntity.ok(splitService.getAllForUser(userId))

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): ResponseEntity<WorkoutSplitResponse> =
        ResponseEntity.ok(splitService.getById(id))

    @PostMapping
    fun create(
        @AuthenticationPrincipal userId: String,
        @RequestBody request: WorkoutSplitRequest
    ): ResponseEntity<WorkoutSplitResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(splitService.create(userId, request))

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: String,
        @RequestBody request: WorkoutSplitRequest
    ): ResponseEntity<WorkoutSplitResponse> =
        ResponseEntity.ok(splitService.update(id, request))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Void> {
        splitService.delete(id)
        return ResponseEntity.noContent().build()
    }

    // --- Split Days ---

    @PostMapping("/{splitId}/days")
    fun addDay(
        @PathVariable splitId: String,
        @RequestBody request: SplitDayRequest
    ): ResponseEntity<SplitDayResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(splitService.addDay(splitId, request))

    @PutMapping("/{splitId}/days/{dayId}")
    fun updateDay(
        @PathVariable splitId: String,
        @PathVariable dayId: String,
        @RequestBody request: SplitDayRequest
    ): ResponseEntity<SplitDayResponse> =
        ResponseEntity.ok(splitService.updateDay(splitId, dayId, request))

    @DeleteMapping("/{splitId}/days/{dayId}")
    fun deleteDay(
        @PathVariable splitId: String,
        @PathVariable dayId: String
    ): ResponseEntity<Void> {
        splitService.deleteDay(splitId, dayId)
        return ResponseEntity.noContent().build()
    }

    // --- Split Day Exercises ---

    @PostMapping("/{splitId}/days/{dayId}/exercises")
    fun addExercise(
        @PathVariable splitId: String,
        @PathVariable dayId: String,
        @RequestBody request: SplitExerciseRequest
    ): ResponseEntity<SplitExerciseResponse> =
        ResponseEntity.status(HttpStatus.CREATED)
            .body(splitService.addExerciseToDay(splitId, dayId, request))

    @PutMapping("/{splitId}/days/{dayId}/exercises/{exerciseId}")
    fun updateExercise(
        @PathVariable splitId: String,
        @PathVariable dayId: String,
        @PathVariable exerciseId: String,
        @RequestBody request: SplitExerciseRequest
    ): ResponseEntity<SplitExerciseResponse> =
        ResponseEntity.ok(splitService.updateExerciseInDay(splitId, dayId, exerciseId, request))

    @DeleteMapping("/{splitId}/days/{dayId}/exercises/{exerciseId}")
    fun removeExercise(
        @PathVariable splitId: String,
        @PathVariable dayId: String,
        @PathVariable exerciseId: String
    ): ResponseEntity<Void> {
        splitService.removeExerciseFromDay(splitId, dayId, exerciseId)
        return ResponseEntity.noContent().build()
    }
}