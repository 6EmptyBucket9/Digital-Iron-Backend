package digitalironbackend.example.demo.service

import digitalironbackend.example.demo.dto.CreateSplitRequest
import digitalironbackend.example.demo.dto.SplitResponse
import digitalironbackend.example.demo.dto.TrainingDayResponse
import digitalironbackend.example.demo.model.TrainingDay
import digitalironbackend.example.demo.model.TrainingSplit
import digitalironbackend.example.demo.repository.TrainingDayRepository
import digitalironbackend.example.demo.repository.TrainingSplitRepository
import digitalironbackend.example.demo.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class TrainingSplitService(
    private val userRepository: UserRepository,
    private val splitRepository: TrainingSplitRepository,
    private val dayRepository: TrainingDayRepository
) {

    fun createSplit(email: String, request: CreateSplitRequest): SplitResponse {
        val user = userRepository.findByEmail(email)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Gebruiker niet gevonden.") }

        val dayNumbers = request.days.map { it.dayNumber }
        if (dayNumbers.size != dayNumbers.distinct().size) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Dagnummers moeten uniek zijn.")
        }

 
        val savedSplit = splitRepository.save(
            TrainingSplit(user = user, name = request.name.trim())
        )


        val savedDays = request.days
            .sortedBy { it.dayNumber }
            .map { dayRequest ->
                dayRepository.save(
                    TrainingDay(
                        split     = savedSplit,
                        dayNumber = dayRequest.dayNumber,
                        name      = dayRequest.name.trim()
                    )
                )
            }

        return SplitResponse(
            id   = savedSplit.id,
            name = savedSplit.name,
            days = savedDays.map { day ->
                TrainingDayResponse(id = day.id, dayNumber = day.dayNumber, name = day.name)
            }
        )
    }

    fun getSplits(email: String): List<SplitResponse> {
        val user = userRepository.findByEmail(email)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Gebruiker niet gevonden.") }

        return splitRepository.findAllByUserId(user.id).map { split ->
            val days = dayRepository.findBySplitIdOrderByDayNumberAsc(split.id)
            SplitResponse(
                id   = split.id,
                name = split.name,
                days = days.map { TrainingDayResponse(it.id, it.dayNumber, it.name) }
            )
        }
    }

    fun getSplit(email: String, splitId: Long): SplitResponse {
        val user = userRepository.findByEmail(email)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Gebruiker niet gevonden.") }

        val split = splitRepository.findById(splitId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Split niet gevonden.") }

        if (split.user.id != user.id) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Geen toegang tot deze split.")
        }

        val days = dayRepository.findBySplitIdOrderByDayNumberAsc(splitId)
        return SplitResponse(
            id   = split.id,
            name = split.name,
            days = days.map { TrainingDayResponse(it.id, it.dayNumber, it.name) }
        )
    }

    fun deleteSplit(email: String, splitId: Long) {
        val user = userRepository.findByEmail(email)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Gebruiker niet gevonden.") }

        val split = splitRepository.findById(splitId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Split niet gevonden.") }

        if (split.user.id != user.id) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Geen toegang tot deze split.")
        }

        splitRepository.delete(split)
    }
}