package cs309.dormiselect.backend.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Team(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: String,
    var leaderId: String,
    var state: State,
) {
    val teamSize: Int
        get() {
            TODO()
        }

    enum class State {
        RECRUITING, NOT_RECRUITING, FULL
    }
}