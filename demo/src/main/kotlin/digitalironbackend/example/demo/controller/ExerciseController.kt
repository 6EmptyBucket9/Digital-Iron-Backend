package digitalironbackend.example.demo.controller

import digitalironbackend.example.demo.model.Exercise
import digitalironbackend.example.demo.service.ExerciseService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/exercises")
class ExerciseController(private val exerciseService: ExerciseService) {

    /**
     * GET /api/v1/exercises
     * Retrieves all exercises available in the system.
     * 
     * Responses:
     *   200 OK – returns a list of Exercise objects
     * 
     * Example response:
     * [
     *   {
     *     "id": 1,
     *     "name": "Bench Press",
     *     "description": "Barbell chest exercise"
     *   },
     *   {
     *     "id": 2,
     *     "name": "Squat",
     *     "description": "Leg compound movement"
     *   }
     * ]
     */
    @GetMapping
    fun getAllExercises(
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<List<Exercise>> {
        val response = exerciseService.getAllExercises()
        return ResponseEntity.ok(response)
    }
}