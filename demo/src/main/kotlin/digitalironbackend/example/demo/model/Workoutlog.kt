package digitalironbackend.example.demo.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "workout_logs")
data class WorkoutLog(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val date: LocalDate = LocalDate.now(),

    @Column(nullable = false)
    val trainingDayName: String,

    @Column
    val notes: String? = null
)