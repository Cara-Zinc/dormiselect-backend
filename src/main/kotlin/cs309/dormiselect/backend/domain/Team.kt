package cs309.dormiselect.backend.domain

import cs309.dormiselect.backend.domain.account.Student
import jakarta.persistence.*


@Entity
class Team(
    @OneToOne(optional = false) var leader: Student,
    var name: String = "${leader.name}'s Team",
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

    var state: State = State.RECRUITING

    val size get() = members.size

    enum class State {
        RECRUITING, NOT_RECRUITING, FULL
    }
}