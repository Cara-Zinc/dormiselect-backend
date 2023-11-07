package cs309.dormiselect.backend.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id


@Entity
class DormitoryExchangeRequest(
    val team1: Team,
    val team2: Team,
) {
    @Id
    @GeneratedValue
    val id: Int? = null
    val state: State
        get() {
            if (approved1 == null || approved2 == null) {
                return State.WAITING
            }
            if (approved1 == true && approved2 == true) {
                return State.APPROVED
            }
            return State.REJECTED
        }

    var approved1: Boolean? = null
        protected set

    var approved2: Boolean? = null
        protected set

    fun approve1(approve: Boolean) {
        approved1 = approve
    }

    fun approve2(approve: Boolean) {
        approved2 = approve
    }

    /*
     Some requirements from the front end partners:

     - An exchange request happens between two teams who already get their dormitories in building A,B.
     - Remind an exchange requests need to be approved by BOTH teachers who are in charge of the A,B buildings where these two teams live.
     - A building may have many teachers in charge. The team who live in building A only need approval from one of them in A and one of teachers in charge of B
     */


    enum class State {
        APPROVED, REJECTED, WAITING
    }
}