package digitalironbackend.example.demo.controller

import digitalironbackend.example.demo.dto.CreateSplitRequest
import digitalironbackend.example.demo.dto.SplitResponse
import digitalironbackend.example.demo.service.TrainingSplitService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/splits")
class TrainingSplitController(private val splitService: TrainingSplitService) {

    /**
     * POST /api/v1/splits
     * Maakt een nieuwe trainingssplit aan voor de ingelogde gebruiker.
     *
     * Request body:
     * {
     *   "name": "Push Pull Legs",
     *   "days": [
     *     { "dayNumber": 1, "name": "Push" },
     *     { "dayNumber": 2, "name": "Pull" },
     *     { "dayNumber": 3, "name": "Legs" }
     *   ]
     * }
     *
     * Responses:
     *   201 Created  – split aangemaakt, geeft SplitResponse terug
     *   400 Bad Request – validatiefouten
     *   401 Unauthorized – niet ingelogd
     */
    @PostMapping
    fun createSplit(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody request: CreateSplitRequest
    ): ResponseEntity<SplitResponse> {
        val response = splitService.createSplit(userDetails.username, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    /**
     * GET /api/v1/splits
     * Geeft alle splits van de ingelogde gebruiker terug.
     *
     * Responses:
     *   200 OK – lijst van SplitResponse
     *   401 Unauthorized
     */
    @GetMapping
    fun getSplits(
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<List<SplitResponse>> {
        val response = splitService.getSplits(userDetails.username)
        return ResponseEntity.ok(response)
    }

    /**
     * GET /api/v1/splits/{id}
     * Geeft één specifieke split terug.
     *
     * Responses:
     *   200 OK – SplitResponse
     *   403 Forbidden – split van een andere gebruiker
     *   404 Not Found – split bestaat niet
     */
    @GetMapping("/{id}")
    fun getSplit(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable id: Long
    ): ResponseEntity<SplitResponse> {
        val response = splitService.getSplit(userDetails.username, id)
        return ResponseEntity.ok(response)
    }

    /**
     * DELETE /api/v1/splits/{id}
     * Verwijdert een split van de ingelogde gebruiker.
     *
     * Responses:
     *   204 No Content – verwijderd
     *   403 Forbidden – split van een andere gebruiker
     *   404 Not Found
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSplit(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        splitService.deleteSplit(userDetails.username, id)
        return ResponseEntity.noContent().build()
    }
}