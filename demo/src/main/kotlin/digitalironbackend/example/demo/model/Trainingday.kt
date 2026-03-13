package digitalironbackend.example.demo.model

import jakarta.persistence.*

@Entity
@Table(name = "training_days")
data class TrainingDay(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "split_id", nullable = false)
    val split: TrainingSplit,

    @Column(nullable = false)
    val dayNumber: Int,           // 1, 2, 3, ...

    @Column(nullable = false)
    val name: String              // e.g. "Push", "Pull", "Legs"

    
)