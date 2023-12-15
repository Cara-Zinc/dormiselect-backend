package cs309.dormiselect.backend.data

data class DormitoryDto(
    val id: Int,
    val roomId: Int,
    val zoneId: Int,
    val size: Int,
    val buildingId: Int,
    val info: String,

)
