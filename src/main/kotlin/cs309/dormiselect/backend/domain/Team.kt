package cs309.dormiselect.backend.domain

import jakarta.persistence.*

@Entity
class Team(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: String,
    @OneToOne var leaderId: Student,
    var state: State,
) {
    val size: Int
        get() {
            TODO()
        }

    enum class State {
        RECRUITING, NOT_RECRUITING, FULL
    }
}