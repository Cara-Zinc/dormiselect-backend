package cs309.dormiselect.backend.controller

import cs309.dormiselect.backend.data.RestResponse
import cs309.dormiselect.backend.domain.Administrator
import cs309.dormiselect.backend.domain.Comment
import cs309.dormiselect.backend.domain.Dormitory
import cs309.dormiselect.backend.domain.Team
import cs309.dormiselect.backend.repo.AccountRepo
import cs309.dormiselect.backend.repo.CommentRepo
import cs309.dormiselect.backend.repo.DormitoryRepo
import cs309.dormiselect.backend.repo.TeamRepo
import org.apache.commons.logging.LogFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api/student")
class StudentController(
    private val teamRepo: TeamRepo,
    private val dormitoryRepo: DormitoryRepo,
    private val commentRepo: CommentRepo
) {
    @GetMapping("/team")
    fun listAllTeam(): List<Team> {
        return teamRepo.findAll().toList()
    }

    @GetMapping("/dorm")
    fun listAllDorm(): List<Dormitory> {
        return dormitoryRepo.findAll().toList()
    }

    @PostMapping("/dorm/{dormitoryId}")
    fun commentOnDorm(
        @PathVariable dormitoryId: String,
        @RequestBody comment: Comment,
    ): ResponseEntity<String> {
        val dormitoryOptional =
            dormitoryRepo.findById(dormitoryId) ?: return ResponseEntity.status(404).body("Dormitory not found")

        commentRepo.save(comment)
        return ResponseEntity.ok("Post comment successfully")
    }

}





