package digitalironbackend.example.demo.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class CreateSplitRequest(

    @field:NotBlank(message = "Naam van de split is verplicht.")
    @field:Size(min = 2, max = 100, message = "Naam moet tussen 2 en 100 tekens zijn.")
    val name: String = "",

    @field:NotEmpty(message = "Voeg minimaal 1 trainingsdag toe.")
    @field:Size(max = 7, message = "Een split mag maximaal 7 dagen bevatten.")
    @field:Valid
    val days: List<TrainingDayRequest> = emptyList()  // ← default waarde zodat Jackson het kan instantiëren
)

data class TrainingDayRequest(

    @field:Min(value = 1, message = "Dagnummer moet minimaal 1 zijn.")
    @field:Max(value = 7, message = "Dagnummer mag maximaal 7 zijn.")
    val dayNumber: Int = 0,

    @field:NotBlank(message = "Naam van de trainingsdag is verplicht.")
    @field:Size(min = 2, max = 100, message = "Naam moet tussen 2 en 100 tekens zijn.")
    val name: String = ""
)