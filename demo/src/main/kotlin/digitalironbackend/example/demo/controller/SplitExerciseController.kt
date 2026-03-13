package digitalironbackend.example.demo.controller

import digitalironbackend.example.demo.dto.SplitExerciseResponse
import digitalironbackend.example.demo.dto.CreateSplitExerciseRequest
import digitalironbackend.example.demo.model.User
import digitalironbackend.example.demo.service.SplitExerciseService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/split-exercises")
class SplitExerciseController(private val splitExerciseService: SplitExerciseService) {

    /**
     * GET /api/v1/split-exercises
     * Retrieves all split exercises for the authenticated user.
     *
     * Responses:
     *   200 OK – returns a list of SplitExerciseResponse
     */
    @GetMapping
    fun getSplitExercises(
        @AuthenticationPrincipal user: User
    ): ResponseEntity<List<SplitExerciseResponse>> {
        val response = splitExerciseService.findBySplitUserId(user.id)
        return ResponseEntity.ok(response)
    }

    /**
     * POST /api/v1/split-exercises
     * Adds a new exercise to a training split for the authenticated user.
     *
     * Request body example (IDs must exist in the database, based on seeder):
     * {
     *   "splitId": 1,
     *   "exerciseId": 1,       // e.g., Bench Press
     *   "targetSets": 5,
     *   "targetReps": 5,
     *   "targetWeight": 160.0,
     *   "orderIndex": 2
     * }
     *
     * Responses:
     *   200 OK – split exercise successfully created, returns SplitExerciseResponse
     *   400 Bad Request – invalid data or missing fields
     *   404 Not Found – split or exercise does not exist
     */
    @PostMapping
    fun addSplitExercise(
        @AuthenticationPrincipal user: User,
        @RequestBody request: CreateSplitExerciseRequest
    ): ResponseEntity<SplitExerciseResponse> {
        val created = splitExerciseService.addSplitExercise(user.id, request)
        return ResponseEntity.ok(created)
    }
}