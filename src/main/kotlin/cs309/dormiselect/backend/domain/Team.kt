package cs309.dormiselect.backend.domain

import jakarta.persistence.*


@Entity
class Team(
    @OneToOne(optional = false) var leader: Student,
) {
    @Id
    @GeneratedValue
    var id: Int? = null
        protected set

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

    val size: Int
        get() {
            TODO()
        }

    enum class State {
        RECRUITING, NOT_RECRUITING, FULL
    }
}