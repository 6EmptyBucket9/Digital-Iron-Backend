package digitalironbackend.example.demo.config

import digitalironbackend.example.demo.model.*
import digitalironbackend.example.demo.repository.*
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class DataSeeder(
    private val passwordEncoder: PasswordEncoder
) {

    @Bean
    fun seedData(
        userRepository: UserRepository,
        exerciseRepository: ExerciseRepository,
        trainingSplitRepository: TrainingSplitRepository,
        trainingDayRepository: TrainingDayRepository,
        splitExerciseRepository: SplitExerciseRepository
    ) = CommandLineRunner {

        // 1. Seed Oefeningen (Verplaatst van ExerciseSeeder naar hier)
        if (exerciseRepository.count() == 0L) {
            val baseExercises = listOf(
                Exercise(name = "Bench Press", description = "Barbell chest exercise"),
                Exercise(name = "Squat", description = "Leg compound movement"),
                Exercise(name = "Deadlift", description = "Back and leg movement")
            )
            exerciseRepository.saveAll(baseExercises)
            println("Seeded base exercises")
        }

        // 2. Seed Users
        if (userRepository.count() == 0L) {
            val encodedPassword = passwordEncoder.encode("password123")
            val john = User(
                name = "John Doe",
                email = "john@example.com",
                password = encodedPassword!!,
                role = Role.USER
            )
            userRepository.save(john)
            println("Seeded John")
        }

      val john = userRepository.findByEmail("john@example.com").orElseThrow()

        // 3. Seed Split & Days
        if (john != null && trainingSplitRepository.count() == 0L) {
            val mySplit = TrainingSplit(name = "Upper/Lower", user = john)
            val savedSplit = trainingSplitRepository.save(mySplit)

            val pushDay = TrainingDay(split = savedSplit, dayNumber = 1, name = "Push")
            trainingDayRepository.save(pushDay)

            // 4. Seed SplitExercise
            val bench = exerciseRepository.findAll().find { it.name == "Bench Press" }
            if (bench != null) {
                val exerciseLink = SplitExercise(
                    split = savedSplit,
                    exercise = bench,
                    target_sets = 3,
                    target_reps = 10,
                    target_weight = 60.0,
                    order_index = 1
                )
                splitExerciseRepository.save(exerciseLink)
                println("Linked Bench Press to John's split")
            }
        }
    }
}