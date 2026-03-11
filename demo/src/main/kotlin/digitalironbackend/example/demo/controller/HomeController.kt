package digitalironbackend.example.demo.controller

import digitalironbackend.example.demo.dto.HomeResponse
import digitalironbackend.example.demo.service.HomeService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/home")
class HomeController(private val homeService: HomeService) {

    /**
     * GET /api/v1/home
     * Returns homepage data for the authenticated user.
     *
     * Headers:
     *   Authorization: Bearer <accessToken>
     *
     * Responses:
     *   200 OK  – returns HomeResponse
     *   401     – missing or invalid token
     *
     * Example response (user has a split, has trained before):
     * {
     *   "lastWorkoutDate": "08-03-2025",
     *   "lastWorkoutDayName": "Push",
     *   "motivationalMessage": "Goed bezig! Jouw laatste training was op 08-03-2025. Tijd voor de volgende! 💪",
     *   "hasSplit": true,
     *   "splitName": "Push Pull Legs",
     *   "nextTrainingDayNumber": 2,
     *   "nextTrainingDayName": "Pull"
     * }
     *
     * Example response (new user, no split, no workouts):
     * {
     *   "lastWorkoutDate": null,
     *   "lastWorkoutDayName": null,
     *   "motivationalMessage": "Begin met het bijhouden van jouw trainingen!",
     *   "hasSplit": false,
     *   "splitName": null,
     *   "nextTrainingDayNumber": null,
     *   "nextTrainingDayName": null
     * }
     */
    @GetMapping
    fun getHomeData(
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<HomeResponse> {
        val response = homeService.getHomeData(userDetails.username)
        return ResponseEntity.ok(response)
    }
}