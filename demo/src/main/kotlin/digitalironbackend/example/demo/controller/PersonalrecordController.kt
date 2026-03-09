package digitalironbackend.example.demo.controller

import digitalironbackend.example.demo.dto.PersonalRecordRequest
import digitalironbackend.example.demo.dto.PersonalRecordResponse
import digitalironbackend.example.demo.service.PersonalRecordService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/prs")
class PersonalRecordController(private val prService: PersonalRecordService) {

    @GetMapping
    fun getAll(@AuthenticationPrincipal userId: String): ResponseEntity<List<PersonalRecordResponse>> =
        ResponseEntity.ok(prService.getAllForUser(userId))

    @GetMapping("/{exerciseId}")
    fun getByExercise(
        @AuthenticationPrincipal userId: String,
        @PathVariable exerciseId: String
    ): ResponseEntity<PersonalRecordResponse> =
        ResponseEntity.ok(prService.getByExercise(userId, exerciseId))

    @PostMapping
    fun create(
        @AuthenticationPrincipal userId: String,
        @RequestBody request: PersonalRecordRequest
    ): ResponseEntity<PersonalRecordResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(prService.create(userId, request))

    @PutMapping("/{id}")
    fun update(
        @AuthenticationPrincipal userId: String,
        @PathVariable id: String,
        @RequestBody request: PersonalRecordRequest
    ): ResponseEntity<PersonalRecordResponse> =
        ResponseEntity.ok(prService.update(userId, id, request))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Void> {
        prService.delete(id)
        return ResponseEntity.noContent().build()
    }
}