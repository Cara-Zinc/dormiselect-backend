package cs309.dormiselect.backend.domain

import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

class TeamJoinRequest(
    @Id @GeneratedValue val id: Int,
    @OneToOne(optional = false) val student: Student,
    val info: String
) {
    fun accept() {
        TODO()
    }

    fun decline() {
        TODO()
    }
}