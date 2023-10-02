package cs309.dormiselect.backend.domain

import jakarta.persistence.*

@Entity
class TeamJoinRequest(
    @Id @GeneratedValue val id: Int,
    @OneToOne(optional = false) val student: Student,
    @ManyToOne(optional = false) val team: Team,
    val info: String
) {
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