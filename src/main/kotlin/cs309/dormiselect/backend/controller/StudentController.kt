package cs309.dormiselect.backend.controller

import cs309.dormiselect.backend.data.RestResponse
import cs309.dormiselect.backend.domain.Administrator
import cs309.dormiselect.backend.domain.Team
import cs309.dormiselect.backend.repo.AccountRepo
import cs309.dormiselect.backend.repo.TeamRepo
import org.apache.commons.logging.LogFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api/student")
class StudentController(private val teamRepo: TeamRepo) {
    @GetMapping("/team")
    fun listAllTeam() : List<Team> {
        return teamRepo.findAll().toList()
    }
}