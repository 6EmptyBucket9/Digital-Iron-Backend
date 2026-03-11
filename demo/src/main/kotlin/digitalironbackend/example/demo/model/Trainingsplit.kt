package digitalironbackend.example.demo.model

import jakarta.persistence.*

@Entity
@Table(name = "training_splits")
data class TrainingSplit(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val name: String,             // e.g. "Push Pull Legs"

    @OneToMany(mappedBy = "split", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @OrderBy("dayNumber ASC")
    val days: List<TrainingDay> = emptyList()
)