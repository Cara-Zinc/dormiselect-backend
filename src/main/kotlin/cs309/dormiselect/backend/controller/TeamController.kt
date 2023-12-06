package cs309.dormiselect.backend.controller;

import cs309.dormiselect.backend.config.CurrentAccount
import cs309.dormiselect.backend.data.RestResponse
import cs309.dormiselect.backend.data.asRestResponse
import cs309.dormiselect.backend.data.message.MessageQueryDto
import cs309.dormiselect.backend.data.message.MessageSendDto
import cs309.dormiselect.backend.domain.*
import cs309.dormiselect.backend.repo.TeamMessageRepo
import cs309.dormiselect.backend.repo.TeamRepo
import cs309.dormiselect.backend.repo.findTeamStudentBelongTo
import cs309.dormiselect.backend.repo.newMessage
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.sql.Timestamp

@RestController("/api/student/team")
class TeamController(
    val teamRepo: TeamRepo,
    val teamMessageRepo: TeamMessageRepo,
) {
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

}
