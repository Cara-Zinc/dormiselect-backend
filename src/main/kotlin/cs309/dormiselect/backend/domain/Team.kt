package cs309.dormiselect.backend.domain

import jakarta.persistence.*

@Entity
class Team(
    @Id @GeneratedValue val id: Int,
    @OneToOne(optional = false) var leader: Student,
    @OneToMany val members: MutableList<Student>,
    @OneToOne var domitory: Domitory?,
    @OneToMany(orphanRemoval = true) val requests: MutableList<TeamJoinRequest> = mutableListOf(),
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