package cs309.dormiselect.backend.data

import cs309.dormiselect.backend.domain.account.Student
import java.sql.Timestamp

data class StudentInfoDto(
    val id: Int,
    val studentId:Long,
    val name: String,
    val password: String,
    val gender: Student.Gender,
    var bedTime: Timestamp?,
    var wakeUpTime: Timestamp?,
    var email: String?,
    var telephone: String?,
    var department: String?,
    var major: String?,
    var qq: String?,
    var wechat: String?,
    var age:Int?,


)