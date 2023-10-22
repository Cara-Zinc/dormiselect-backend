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

     - An exchange request happens between two teams who already get their dormitories.
     - Remind an exchange requests need to be approved by BOTH teachers who are in charge of the buildings where these two teams live.
     - Usually, a building has one teacher in charge, but teachers can change during the request
     */


    enum class State {
        APPROVED, REJECTED, WAITING
    }
}