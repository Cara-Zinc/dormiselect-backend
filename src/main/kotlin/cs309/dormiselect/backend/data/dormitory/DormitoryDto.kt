package cs309.dormiselect.backend.data.dormitory

import cs309.dormiselect.backend.domain.Comment
import cs309.dormiselect.backend.domain.account.Student

data class DormitoryDto(
    val id: Int,
    val roomId: Int,
    val zoneId: Int,
    val size: Int,
    val buildingId: Int,
    val gender: Student.Gender,
    val comments: MutableList<Comment> = mutableListOf(),
    val info: String,

    )
