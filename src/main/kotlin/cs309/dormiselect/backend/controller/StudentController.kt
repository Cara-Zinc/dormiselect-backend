package cs309.dormiselect.backend.controller

import cs309.dormiselect.backend.config.CurrentAccount
import cs309.dormiselect.backend.data.RestResponse
import cs309.dormiselect.backend.data.asRestResponse
import cs309.dormiselect.backend.domain.Announcement
import cs309.dormiselect.backend.domain.Comment
import cs309.dormiselect.backend.domain.Dormitory
import cs309.dormiselect.backend.domain.Team
import cs309.dormiselect.backend.domain.account.Account
import cs309.dormiselect.backend.repo.*
import org.springframework.web.bind.annotation.*
import kotlin.jvm.optionals.getOrElse

@RestController
@RequestMapping("/api/student")
class StudentController(
    private val teamRepo: TeamRepo,
    private val dormitoryRepo: DormitoryRepo,
    private val commentRepo: CommentRepo,
    private val studentRepo: StudentRepo,
    private val announcementRepo: AnnouncementRepo,
) {
    @GetMapping("/team")
    fun listAllTeam(): RestResponse<List<Team>?> {
        return teamRepo.findAll().toList().asRestResponse()
    }

    @GetMapping("/dorm")
    fun listAllDorm(): RestResponse<List<Dormitory>?> {
        return dormitoryRepo.findAll().toList().asRestResponse()
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

    @GetMapping("/favorited")
    fun listAllFavorite(
        @CurrentAccount account: Account
    ): RestResponse<List<Dormitory>?> {

        val student = studentRepo.findByName(account.name)
        val teamList = teamRepo.findByMembersContaining(student)
        if (teamList.isEmpty()) {
            return RestResponse.fail(404, "This student hasn't join any team")
        }
        return teamList[0].favorites.asRestResponse()
    }

    @GetMapping("/announcement")
    fun viewAnnouncement(): RestResponse<List<Announcement>?> {
        return announcementRepo.findAll().toList().asRestResponse()
    }

    @GetMapping("/select")
    fun selectDorm(
        @CurrentAccount account: Account,
        @PathVariable teamId: String,
    ): RestResponse<Any?> {
        val student = studentRepo.findByName(account.name)
        val teamList = teamRepo.findByMembersContaining(student)
        val team = teamList[0]
        if (team.leader.studentId!=student.studentId){
            return RestResponse.fail(404,"Current user is not the team leader")
        }
        return RestResponse.fail(404,"Don't know the dormitory id")
    }
}





