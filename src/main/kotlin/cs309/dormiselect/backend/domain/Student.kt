package cs309.dormiselect.backend.domain

import jakarta.persistence.*
import java.sql.Timestamp

@Entity
class Student(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: String,
    @OneToOne val account: Account,
    var gender: Gender,
    var bedtime: Timestamp,
    var wakeUpTime: Timestamp,
    val hobbies: MutableSet<String>
) {
    enum class Gender {
        MALE, FEMALE
        // PREFER_NOT_TO_SAY can be added into the enum class but since gender-mixed accommodation is restricted, we cancel this gender
    }
}