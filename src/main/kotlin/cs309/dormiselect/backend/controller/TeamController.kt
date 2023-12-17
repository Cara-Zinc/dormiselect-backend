package cs309.dormiselect.backend.controller;

import cs309.dormiselect.backend.config.CurrentAccount
import cs309.dormiselect.backend.data.RestResponse
import cs309.dormiselect.backend.data.asRestResponse
import cs309.dormiselect.backend.data.message.MessageQueryDto
import cs309.dormiselect.backend.data.message.MessageSendDto
import cs309.dormiselect.backend.data.team.TeamJoinDto
import cs309.dormiselect.backend.domain.Dormitory
import cs309.dormiselect.backend.domain.Team
import cs309.dormiselect.backend.domain.TeamJoinRequest
import cs309.dormiselect.backend.domain.TeamMessage
import cs309.dormiselect.backend.domain.account.Account
import cs309.dormiselect.backend.domain.account.Administrator
import cs309.dormiselect.backend.domain.account.Student
import cs309.dormiselect.backend.domain.account.Teacher
import cs309.dormiselect.backend.repo.*
import org.springframework.web.bind.annotation.*
import java.sql.Timestamp

@RestController
@RequestMapping("/resources/student/team")
class TeamController(
    val studentRepo: StudentRepo,
    val teamRepo: TeamRepo,
    val teamMessageRepo: TeamMessageRepo,
    val teamJoinRequestRepo: TeamJoinRequestRepo,
) {
    @GetMapping("/list")
    fun listAllTeam(): RestResponse<List<Team>?> {
        return teamRepo.findAll().toList().asRestResponse()
    }

    @PostMapping("/message/query")
    fun getMessage(
        @CurrentAccount account: Account,
        @RequestBody body: MessageQueryDto
    ): RestResponse<List<TeamMessage>?> {
        val queryTeamId = when (account) {
            is Teacher, is Administrator -> body.receiverId ?: throw IllegalArgumentException("team id is required")
            is Student -> body.receiverId ?: teamRepo.findTeamStudentBelongTo(account)?.id
            ?: throw IllegalArgumentException("you have not joined any team")

            else -> throw IllegalArgumentException("account type not supported")
        }

        return teamMessageRepo.findAllNotHiddenByReceiverId(queryTeamId).filter {
            it.timestamp >= Timestamp(body.timeFrom ?: 0) && (it.timestamp <= Timestamp(body.timeTo ?: Long.MAX_VALUE))
        }.asRestResponse()
    }

    @PostMapping("/message/send")
    fun sendMessage(@CurrentAccount account: Account, @RequestBody body: MessageSendDto): RestResponse<Any?> {
        if (account !is Student) {
            throw IllegalArgumentException("only student can send team message")
        }

        val team =
            teamRepo.findTeamStudentBelongTo(account) ?: throw IllegalArgumentException("you have not joined any team")
        teamMessageRepo.newMessage(account, team, body.message)
        return RestResponse.success(null, "message sent")
    }

    @GetMapping("/favorite")
    fun listAllFavorite(
        @CurrentAccount account: Account,
        @RequestParam teamId: Int,
    ): RestResponse<List<Dormitory>?> {
        val team = when (account) {
            is Teacher, is Administrator -> teamRepo.findById(teamId)
                .orElseThrow { IllegalArgumentException("team not found") }

            is Student -> teamRepo.findTeamStudentBelongTo(account)
                ?: throw IllegalArgumentException("you have not joined any team")

            else -> throw IllegalArgumentException("account type not supported")
        }
        return team.favorites.asRestResponse()
    }

    @PostMapping("/apply")
    fun applyJoinTeam(@CurrentAccount account: Account, @RequestBody teamJoinDto: TeamJoinDto): RestResponse<Any?> {
        if (account !is Student) {
            throw IllegalArgumentException("only student can apply to join team")
        }

        val team = teamRepo.findById(teamJoinDto.teamId).orElseThrow { IllegalArgumentException("team not found") }
        teamJoinRequestRepo.newRequest(teamRepo, team, account, teamJoinDto.info)

        return RestResponse.success(null, "request sent")
    }

    @GetMapping("/apply/list")
    fun listAllMyJoinRequest(
        @CurrentAccount account: Account,
        @RequestParam studentId: Int?
    ): RestResponse<List<TeamJoinRequest>?> {
        return when (account) {
            is Teacher, is Administrator -> teamJoinRequestRepo.findAllByStudentId(
                studentId ?: throw IllegalArgumentException("student id is required")
            )

            is Student -> teamJoinRequestRepo.findAllByStudentId(account.id!!)
            else -> throw IllegalArgumentException("account type not supported")
        }.asRestResponse()
    }

    @PostMapping("/apply/{requestId}/cancel")
    fun cancelJoinTeam(@CurrentAccount account: Account, @PathVariable requestId: Int): RestResponse<Any?> {
        val request =
            teamJoinRequestRepo.findById(requestId).orElseThrow { IllegalArgumentException("request not found") }
        if (request.student != account) {
            return RestResponse.fail(403, "you are not the student of this request")
        }

        teamJoinRequestRepo.cancelRequest(requestId)
        return RestResponse.success(null, "request cancelled")
    }

    @GetMapping("/request/list")
    fun listAllJoinRequestForTeam(
        @CurrentAccount account: Account,
        @RequestParam teamId: Int?
    ): RestResponse<List<TeamJoinRequest>?> {
        return when (account) {
            is Teacher, is Administrator -> teamJoinRequestRepo.findAllByTeamId(
                teamId ?: throw IllegalArgumentException("team id is required")
            )

            is Student -> {
                val team = teamRepo.findTeamStudentLeads(account)
                    ?: throw IllegalArgumentException("you are not the leader of any team")
                teamJoinRequestRepo.findAllByTeamId(team.id!!)
            }

            else -> throw IllegalArgumentException("account type not supported")
        }.asRestResponse()
    }

    @PostMapping("/request/{requestId}/accept")
    fun acceptJoinTeam(@CurrentAccount account: Account, @PathVariable requestId: Int): RestResponse<Any?> {
        val request =
            teamJoinRequestRepo.findById(requestId).orElseThrow { IllegalArgumentException("request not found") }
        if (request.team.leader != account) {
            return RestResponse.fail(403, "you are not the leader of the team of this request")
        }

        teamJoinRequestRepo.acceptRequest(teamRepo, requestId)
        return RestResponse.success(null, "request accepted")
    }

    @PostMapping("/request/{requestId}/decline")
    fun declineJoinTeam(@CurrentAccount account: Account, @PathVariable requestId: Int): RestResponse<Any?> {
        val request =
            teamJoinRequestRepo.findById(requestId).orElseThrow { IllegalArgumentException("request not found") }
        if (request.team.leader != account) {
            return RestResponse.fail(403, "you are not the leader of the team of this request")
        }

        teamJoinRequestRepo.declineRequest(requestId)
        return RestResponse.success(null, "request declined")
    }
}
