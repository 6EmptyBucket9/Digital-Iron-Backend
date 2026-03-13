package digitalironbackend.example.demo.model

import jakarta.persistence.*

@Entity
@Table(name = "exercises")
data class Exercise(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,             // e.g. "Bench Press", "Squat", "Deadlift"
    @Column(nullable = true)
    val description: String? = null,  // Optional description of the exercise
    @Column(nullable = true)
    val created_by_user_id: Long? = null // Optional reference to the user who created this exercise (if you want user-generated exercises)

    
)