package cs309.dormiselect.backend.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import java.sql.Timestamp

@Entity
class Student(
    @Id @GeneratedValue val id: Int,
    @OneToOne(optional = false) val account: Account,
    var gender: Gender,
    var bedtime: Timestamp,
    var wakeUpTime: Timestamp,
    val hobbies: MutableSet<String> = mutableSetOf(),
    @OneToOne(orphanRemoval = true) var joinRequest: TeamJoinRequest? = null,
) {
    enum class Gender {
        MALE, FEMALE
        // PREFER_NOT_TO_SAY can be added into the enum class but since gender-mixed accommodation is restricted, we cancel this gender
    }
}