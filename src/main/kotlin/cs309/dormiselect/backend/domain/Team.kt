package cs309.dormiselect.backend.domain

import jakarta.persistence.*


@Entity
class Team(
    @OneToOne(optional = false) var leader: Student,
) {
    @Id
    @GeneratedValue
    val id: Int? = null

    @OneToMany
    @Suppress("LeakingThis")
    val members: MutableList<Student> = mutableListOf(leader)

    @ManyToMany
    val favorites: MutableList<Dormitory> = mutableListOf()

    @OneToOne
    var dormitory: Dormitory? = null

    @OneToMany(orphanRemoval = true)
    val requests: MutableList<TeamJoinRequest> = mutableListOf()

    var state: State = State.RECRUITING

    val size get() = members.size

    enum class State {
        RECRUITING, NOT_RECRUITING, FULL
    }
}