package cs309.dormiselect.backend.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Team(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var studentId:String,
    var leaderId: String,
    var teamSize: Int,
    var state: State,
) {
    enum class State{
        RECRUTING,FULL
    }
}