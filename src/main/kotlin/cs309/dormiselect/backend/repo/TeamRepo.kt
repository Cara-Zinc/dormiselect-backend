package cs309.dormiselect.backend.repo

import cs309.dormiselect.backend.domain.Dormitory
import cs309.dormiselect.backend.domain.Team
import cs309.dormiselect.backend.domain.account.Student
import org.springframework.data.repository.CrudRepository

interface TeamRepo : CrudRepository<Team, Int>{
    fun findByMembersContaining(member: Student): List<Team>

    fun findByDormitory(dormitory: Dormitory): List<Team>

    fun findByState(state: Team.State): List<Team>

    fun findByFavoritesContaining(favoriteDormitory: Dormitory): List<Team>

    //Size does not actually exist in Team database, because it is a getter!
    //fun findBySize(size: Int): List<Team>

    fun findByLeaderName(name: String): Team?

    fun findByLeaderNameIgnoreCase(name: String): Team?

    //no need to implement pre-declared function findAll(): List<Team>

}

fun TeamRepo.newTeam(leader: Student, name: String = "${leader.name}'s Team") = Team(leader, name).also { save(it) }

fun TeamRepo.findTeamStudentBelongTo(student: Student) = findByMembersContaining(student).firstOrNull()