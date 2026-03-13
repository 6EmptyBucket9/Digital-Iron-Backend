// package digitalironbackend.example.demo.config

// import digitalironbackend.example.demo.model.*
// import digitalironbackend.example.demo.repository.*
// import org.springframework.boot.CommandLineRunner
// import org.springframework.context.annotation.Bean
// import org.springframework.context.annotation.Configuration
// import org.springframework.security.crypto.password.PasswordEncoder

// @Configuration
// class DataSeeder(
//     private val passwordEncoder: PasswordEncoder
// ) {

//     @Bean
//     fun seedData(
//         userRepository: UserRepository,
//         exerciseRepository: ExerciseRepository,
//         trainingSplitRepository: TrainingSplitRepository,
//         trainingDayRepository: TrainingDayRepository,
//         splitExerciseRepository: SplitExerciseRepository
//     ) = CommandLineRunner {

//         if (userRepository.count() == 0L) {
//             val john = User(
//                 name = "John Doe",
//                 email = "john@example.com",
//                 password = passwordEncoder.encode("password123"),
//                 role = Role.USER
//             )
//             userRepository.save(john)
//             println("Seeded John")
//         }

//         // Gebruik .orElseThrow() om de Optional<User> om te zetten naar een User object
//         val john = userRepository.findByEmail("john@example.com") 
//             ?: throw IllegalStateException("User john@example.com not found")

//         if (trainingSplitRepository.count() == 0L) {
//             val pushPullLegs = TrainingSplit(
//                 name = "Push Pull Legs",
//                 user = john // Werkt nu omdat john een User is, geen Optional<User>
//             )
//             val savedSplit = trainingSplitRepository.save(pushPullLegs)

//             val day1 = TrainingDay(split = savedSplit, dayNumber = 1, name = "Push Day")
//             trainingDayRepository.save(day1)

//             val benchPress = exerciseRepository.findAll().find { it.name == "Bench Press" }

//             if (benchPress != null) {
//                 val benchEntry = SplitExercise(
//                     split = savedSplit, // Zorg dat SplitExercise.kt @ManyToOne gebruikt!
//                     exercise = benchPress,
//                     target_sets = 3,
//                     target_reps = 10,
//                     target_weight = 60.0,
//                     order_index = 1
//                 )
//                 splitExerciseRepository.save(benchEntry)
//             }
//         }
//     }
// }