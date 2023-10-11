package cs309.dormiselect.backend.domain

import jakarta.persistence.*

@Entity
class Team(
    @OneToOne(optional = false) var leader: Student,
    @OneToMany val members: MutableList<Student>,
    @OneToOne var domitory: Domitory?,
    @ManyToMany val favorites: MutableList<Domitory> = mutableListOf(),
    @OneToMany(orphanRemoval = true) val requests: MutableList<TeamJoinRequest> = mutableListOf(),
    var state: State,
    @Id @GeneratedValue val id: Int? = null,
) {
    val size: Int
        get() {
            TODO()
        }

    enum class State {
        RECRUITING, NOT_RECRUITING, FULL
    }
}