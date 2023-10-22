package cs309.dormiselect.backend.domain

import jakarta.persistence.*


@Entity
class DormitoryExchangeRequest(
    @OneToOne(optional = false) val team1: Team,
    @OneToOne(optional = false) val team2: Team,

) {
    @Id
    @GeneratedValue
    val id: Int? = null
    var state: State = State.WAITING

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