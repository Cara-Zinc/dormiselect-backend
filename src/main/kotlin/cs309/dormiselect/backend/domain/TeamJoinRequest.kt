package cs309.dormiselect.backend.domain

import cs309.dormiselect.backend.domain.account.Student
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class TeamJoinRequest(
    @ManyToOne(optional = false) val student: Student,
    @ManyToOne(optional = false) val team: Team,
    val info: String,
) {
    @Id
    @GeneratedValue
    val id: Int? = null
    var state: State = State.WAITING
    fun accept() {
        team.members += student
        state = State.ACCEPT
    }

    fun decline() {
        state = State.DECLINE
    }

    fun cancel() {
        state = State.CANCELLED
    }

    enum class State {
        ACCEPT, DECLINE, WAITING, CANCELLED
    }
}