package cs309.dormiselect.backend.controller

import cs309.dormiselect.backend.data.RestResponse
import cs309.dormiselect.backend.domain.Comment
import cs309.dormiselect.backend.domain.Dormitory
import cs309.dormiselect.backend.domain.Team
import cs309.dormiselect.backend.repo.CommentRepo
import cs309.dormiselect.backend.repo.DormitoryRepo
import cs309.dormiselect.backend.repo.TeamRepo
import org.springframework.web.bind.annotation.*
import kotlin.jvm.optionals.getOrElse

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
        @PathVariable dormitoryId: Int,
        @RequestBody comment: Comment,
    ): RestResponse<Any?> {
        val dormitory =
            dormitoryRepo.findById(dormitoryId)
                .getOrElse { return RestResponse.fail(404, "Dormitory not found") }

        commentRepo.save(comment)
        return RestResponse.success(null, "Post comment successfully")
    }

}





