package digitalironbackend.example.demo.dto

data class HomeResponse(

    // Last workout info — null if user has never trained
    val lastWorkoutDate: String?,         // "DD-MM-YYYY" format, null if none
    val lastWorkoutDayName: String?,      // e.g. "Push", null if none
    val motivationalMessage: String,      // Always present

    // Training split info
    val hasSplit: Boolean,
    val splitName: String?,               // null if no split
    val nextTrainingDayNumber: Int?,      // null if no split
    val nextTrainingDayName: String?,     // e.g. "Pull Day", null if no split
)