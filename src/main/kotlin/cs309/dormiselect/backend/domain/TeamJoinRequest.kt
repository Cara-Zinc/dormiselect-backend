package cs309.dormiselect.backend.domain

import jakarta.persistence.*

@Entity
class TeamJoinRequest(
    @OneToOne(optional = false) val student: Student,
    @ManyToOne(optional = false) val team: Team,
    val info: String,
) {
    @Id
    @GeneratedValue
    val id: Int? = null

    fun accept() {
        TODO()
    }

    fun decline() {
        TODO()
    }

    fun cancel() {
        TODO()
    }
}