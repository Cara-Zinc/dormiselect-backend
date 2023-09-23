package cs309.dormiselect.backend.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.sql.Timestamp

@Entity
class StudentInfo(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val studentId: String,
    var name: String,
    var gender: Gender,
    var bedtime: Timestamp,
    var wakeUpTime: Timestamp,

    ) {
    enum class Gender {
        MALE, FEMALE
        // PREFER_NOT_TO_SAY can be added into the enum class but since gender-mixed accommodation is restricted, we cancel this gender
    }
}