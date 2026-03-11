package digitalironbackend.example.demo.service

import digitalironbackend.example.demo.dto.HomeResponse
import digitalironbackend.example.demo.repository.TrainingSplitRepository
import digitalironbackend.example.demo.repository.UserRepository
import digitalironbackend.example.demo.repository.WorkoutLogRepository
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Service
class HomeService(
    private val userRepository: UserRepository,
    private val workoutLogRepository: WorkoutLogRepository,
    private val trainingSplitRepository: TrainingSplitRepository
) {

    private val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    fun getHomeData(email: String): HomeResponse {

        // ── 1. Resolve user ───────────────────────────────────────────
      val user = userRepository.findByEmail(email)
    .orElseThrow { IllegalArgumentException("Gebruiker niet gevonden.") }
        // ── 2. Last workout ───────────────────────────────────────────
        val lastWorkout = workoutLogRepository.findTopByUserIdOrderByDateDesc(user.id)

        val lastWorkoutDate     = lastWorkout.map { it.date.format(dateFormatter) }.orElse(null)
        val lastWorkoutDayName  = lastWorkout.map { it.trainingDayName }.orElse(null)

        val motivationalMessage = if (lastWorkout.isPresent) {
            "Goed bezig! Jouw laatste training was op ${lastWorkoutDate}. Tijd voor de volgende! 💪"
        } else {
            "Begin met het bijhouden van jouw trainingen!"
        }

        // ── 3. Training split ─────────────────────────────────────────
        val split = trainingSplitRepository.findFirstByUserId(user.id)

        if (split.isEmpty) {
            return HomeResponse(
                lastWorkoutDate    = lastWorkoutDate,
                lastWorkoutDayName = lastWorkoutDayName,
                motivationalMessage = motivationalMessage,
                hasSplit           = false,
                splitName          = null,
                nextTrainingDayNumber = null,
                nextTrainingDayName   = null
            )
        }

        val activeSplit = split.get()
        val days        = activeSplit.days

        // ── 4. Determine next training day ────────────────────────────
        // Strategy: find which day was done last, then pick the next one.
        // If no workouts yet, start at day 1.
        val nextDay = if (lastWorkout.isEmpty) {
            days.firstOrNull()
        } else {
            val lastDayName = lastWorkout.get().trainingDayName
            val lastDayIndex = days.indexOfFirst { it.name == lastDayName }
            if (lastDayIndex == -1 || lastDayIndex == days.size - 1) {
                days.firstOrNull()          // wrap around to day 1
            } else {
                days[lastDayIndex + 1]      // next day in sequence
            }
        }

        return HomeResponse(
            lastWorkoutDate       = lastWorkoutDate,
            lastWorkoutDayName    = lastWorkoutDayName,
            motivationalMessage   = motivationalMessage,
            hasSplit              = true,
            splitName             = activeSplit.name,
            nextTrainingDayNumber = nextDay?.dayNumber,
            nextTrainingDayName   = nextDay?.name
        )
    }
}