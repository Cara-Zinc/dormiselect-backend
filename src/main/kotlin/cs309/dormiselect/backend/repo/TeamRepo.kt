package cs309.dormiselect.backend.repo

import cs309.dormiselect.backend.domain.Dormitory
import cs309.dormiselect.backend.domain.Student
import cs309.dormiselect.backend.domain.Team
import org.springframework.data.repository.CrudRepository
interface TeamRepo : CrudRepository<Team, Int>{
    fun findByMembersContaining(member: Student): List<Team>

    fun findByDormitory(dormitory: Dormitory): List<Team>

    fun findByState(state: Team.State): List<Team>

    fun findByFavoritesContaining(favoriteDormitory: Dormitory): List<Team>

    fun findBySize(size: Int): List<Team>

    fun findByLeaderName(name: String): Team?

    fun findByLeaderNameIgnoreCase(name: String): Team?

    //no need to implement pre-declared function findAll(): List<Team>

}