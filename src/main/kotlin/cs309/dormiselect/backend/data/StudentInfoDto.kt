package cs309.dormiselect.backend.data

import java.sql.Timestamp

data class StudentInfoDto(
    val studentId:Int,
    val name: String,
    val gender: Gender,
    var bedTime: Timestamp?,
    var wakeUpTime: Timestamp?,
    var email: String?,
    var telephone: String?,
    var department: String,
    var major: String?,
    val hasJoinTeam: Boolean,
    var qq: String?,
    var wechat: String?,
    var age:Int?,
    val hobbies: MutableSet<String>

)
enum class Gender {
    MALE, FEMALE
    // PREFER_NOT_TO_SAY can be added into the enum class but since gender-mixed accommodation is restricted, we cancel this gender
}