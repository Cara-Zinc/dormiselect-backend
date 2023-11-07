package cs309.dormiselect.backend.repo

import cs309.dormiselect.backend.domain.Student
import cs309.dormiselect.backend.domain.Team
import cs309.dormiselect.backend.domain.TeamJoinRequest
import org.springframework.data.repository.CrudRepository

interface TeamJoinRequestRepo : CrudRepository<TeamJoinRequest, Int> {
    fun findAllByTeam(team: Team): List<TeamJoinRequest>
    fun findAllByStudent(student: Student): List<TeamJoinRequest>
}

