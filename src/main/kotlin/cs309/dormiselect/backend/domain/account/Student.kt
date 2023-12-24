package cs309.dormiselect.backend.domain.account

import jakarta.persistence.Entity
import java.sql.Timestamp

@Entity
class Student(
    val studentId: Long,
    name: String,
    password: String,
    var gender: Gender,
) : Account(name, password) {

    enum class Gender {
        MALE, FEMALE
        // PREFER_NOT_TO_SAY can be added into the enum class but since gender-mixed accommodation is restricted, we cancel this gender
    }

    var bedTime: String? = null
    var wakeUpTime: String? = null
    val hobbies: MutableSet<String> = mutableSetOf()
    var email: String? = null
    var telephone: String? = null
    var department: String? = null
    var major: String? = null
    var qq: String? = null
    var wechat: String? = null
    var age: Int? = null

}