package digitalironbackend.example.demo.model

import jakarta.persistence.*

@Entity
@Table(name = "split_exercises")
data class SplitExercise(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "split_id", nullable = false)
    val split: TrainingSplit,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    val exercise: Exercise,

    @Column(nullable = false)
    val target_sets: Int,
    @Column(nullable = false)
    val target_reps: Int,
    @Column(nullable = false)
    val target_weight: Double,
    @Column(nullable = false)
    val order_index: Int

)   