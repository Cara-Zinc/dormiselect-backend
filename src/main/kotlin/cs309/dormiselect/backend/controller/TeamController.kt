package cs309.dormiselect.backend.controller;

import cs309.dormiselect.backend.config.CurrentAccount
import cs309.dormiselect.backend.data.*
import cs309.dormiselect.backend.data.message.MessageQueryDto
import cs309.dormiselect.backend.data.message.MessageSendDto
import cs309.dormiselect.backend.data.team.TeamCreateDto
import cs309.dormiselect.backend.data.team.TeamJoinDto
import cs309.dormiselect.backend.domain.Dormitory
import cs309.dormiselect.backend.domain.TeamJoinRequest
import cs309.dormiselect.backend.domain.TeamMessage
import cs309.dormiselect.backend.domain.account.Account
import cs309.dormiselect.backend.domain.account.Administrator
import cs309.dormiselect.backend.domain.account.Student
import cs309.dormiselect.backend.domain.account.Teacher
import cs309.dormiselect.backend.repo.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.sql.Timestamp

@RestController
@RequestMapping("/api/student/team")
class TeamController(
    val studentRepo: StudentRepo,
    val teamRepo: TeamRepo,
    val teamMessageRepo: TeamMessageRepo,
    val teamJoinRequestRepo: TeamJoinRequestRepo,
    val dormitoryRepo: DormitoryRepo,
) {
    @GetMapping("/list")
    fun listAllTeam(
        @ModelAttribute pageInfo: PageInfo
    ): RestResponse<Any?> {
        val pageable: Pageable = PageRequest.of(pageInfo.page - 1, pageInfo.pageSize)
        val resultPage = teamRepo.findAll(pageable)

        if (pageInfo.page > resultPage.totalPages) {
            return TeamListDto(resultPage.totalPages, pageInfo.page, pageInfo.pageSize, listOf()).asRestResponse()
        }
        val teamList = TeamListDto(resultPage.totalPages, pageInfo.page, pageInfo.pageSize, resultPage.content)
        return teamList.asRestResponse()
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

    @GetMapping("/favorite-list")
    fun listAllFavorite(
        @CurrentAccount account: Account,
        @RequestParam teamId: Int?,
        @ModelAttribute pageInfo: PageInfo,
    ): RestResponse<PageResult<Dormitory>?> {
        val page = PageRequest.of(pageInfo.page - 1, pageInfo.pageSize)
        val team = when (account) {
            is Teacher, is Administrator -> teamRepo.findById(
                teamId ?: throw IllegalArgumentException("teamId required")
            )
                .orElseThrow { IllegalArgumentException("team not found") }

            is Student -> teamRepo.findTeamStudentBelongTo(account)
                ?: throw IllegalArgumentException("you have not joined any team")

            else -> throw IllegalArgumentException("account type not supported")
        }
        return team.favorites.toPageResult(page).asRestResponse()
    }

    @PostMapping("/apply")
    fun applyJoinTeam(@CurrentAccount account: Account, @RequestBody teamJoinDto: TeamJoinDto): RestResponse<Any?> {
        if (account !is Student) {
            throw IllegalArgumentException("only student can apply to join team")
        }

        val team = teamRepo.findById(teamJoinDto.teamId).orElseThrow { IllegalArgumentException("team not found") }
        require(account.gender == team.gender) {
            "You can't apply for a team which is different form your gender."
        }
        teamJoinRequestRepo.newRequest(teamRepo, team, account, teamJoinDto.info)

        return RestResponse.success(null, "request sent")
    }

    @GetMapping("/apply/list")
    fun listAllMyJoinRequest(
        @CurrentAccount account: Account,
        @ModelAttribute pageInfo: PageInfo,
    ): RestResponse<PageResult<TeamJoinRequest>?> {
        val pageable: Pageable = PageRequest.of(pageInfo.page - 1, pageInfo.pageSize)
        return when (account) {
            is Teacher, is Administrator -> teamJoinRequestRepo.findAllByStudentId(
                account.id ?: throw IllegalArgumentException("student id is required"), pageable
            ).toPageResult()

            is Student -> teamJoinRequestRepo.findAllByStudentId(account.id!!, pageable).toPageResult()
            else -> throw IllegalArgumentException("account type not supported")
        }.asRestResponse()
    }

    @PostMapping("/cancel-apply")
    fun cancelJoinTeam(
        @CurrentAccount account: Account,
        @RequestBody integerWrapper: IntegerWrapper
    ): RestResponse<Any?> {
        val request =
            teamJoinRequestRepo.findById(integerWrapper.id)
                .orElseThrow { IllegalArgumentException("request not found") }
        if (request.student.id != account.id) {
            return RestResponse.fail(403, "you are not the student of this request")
        }

        teamJoinRequestRepo.cancelRequest(integerWrapper.id)
        return RestResponse.success(null, "request cancelled")
    }

    @GetMapping("/request/list")
    fun listAllJoinRequestForTeam(
        @CurrentAccount account: Account,
        @ModelAttribute pageInfo: PageInfo,
    ): RestResponse<PageResult<TeamJoinRequest>?> {
        val pageable = PageRequest.of(pageInfo.page - 1, pageInfo.pageSize)
        val teamId = teamRepo.findByLeaderId(account.id!!).firstOrNull()?.id
        return when (account) {
            is Teacher, is Administrator -> teamJoinRequestRepo.findAllByTeamId(
                teamId ?: throw IllegalArgumentException("team id is required"),
                pageable
            ).toPageResult()

            is Student -> {
                val team = teamRepo.findTeamStudentLeads(account)
                    ?: throw IllegalArgumentException("you are not the leader of any team")
                teamJoinRequestRepo.findAllByTeamId(team.id!!, pageable).toPageResult()
            }

            else -> throw IllegalArgumentException("account type not supported")
        }.asRestResponse()
    }

    @PostMapping("/accept-apply")
    fun acceptJoinTeam(
        @CurrentAccount account: Account,
        @RequestBody integerWrapper: IntegerWrapper
    ): RestResponse<Any?> {
        val requestId = integerWrapper.id
        val request =
            teamJoinRequestRepo.findById(requestId).orElseThrow { IllegalArgumentException("request not found") }
        if (request.team.leader.id != account.id) {
            return RestResponse.fail(403, "you are not the leader of the team of this request")
        }

        teamJoinRequestRepo.acceptRequest(teamRepo, requestId)
        return RestResponse.success(null, "request accepted")
    }

    @PostMapping("/decline-apply")
    fun declineJoinTeam(
        @CurrentAccount account: Account,
        @RequestBody integerWrapper: IntegerWrapper
    ): RestResponse<Any?> {
        val requestId = integerWrapper.id
        val request =
            teamJoinRequestRepo.findById(requestId).orElseThrow { IllegalArgumentException("request not found") }
        if (request.team.leader.id != account.id) {
            return RestResponse.fail(403, "you are not the leader of the team of this request")
        }

        teamJoinRequestRepo.declineRequest(requestId)
        return RestResponse.success(null, "request declined")
    }

    @PostMapping("/create")
    fun createTeam(
        @CurrentAccount account: Account,
        @RequestBody teamCreateDto: TeamCreateDto,
    ): RestResponse<Any?> {
        require(account is Student) {
            "Only student can create a team"
        }
        require(teamRepo.findTeamStudentBelongTo(account) == null) {
            "You have already joined a team"
        }
        val team = teamRepo.newTeam(account, teamCreateDto.name)
        team.apply {
            maxSize = teamCreateDto.maxSize
            introduction = teamCreateDto.introduction
            recruiting = teamCreateDto.recruiting
        }

        return RestResponse.success(null, "Successfully create a team")
    }

    @GetMapping("/info")
    fun fetchTeamInfo(
        @CurrentAccount account: Account
    ): RestResponse<Any?> {
        return teamRepo.findTeamStudentBelongTo(account.id!!).asRestResponse()
    }

    @PostMapping("/edit")
    fun editTeamInfo(
        @CurrentAccount account: Account,
        @RequestBody body: TeamCreateDto,
    ): RestResponse<Nothing?> {
        require(account is Student) {
            "Only student can edit a team"
        }

        val team = teamRepo.findTeamStudentLeads(account)
            ?: throw IllegalArgumentException("You are not the leader of any team")
        team.apply {
            maxSize = body.maxSize
            introduction = body.introduction
            recruiting = body.recruiting
        }

        teamRepo.save(team)

        return RestResponse.success(null, "Successfully edited a team")
    }

    @PostMapping("collect-dormitory")
    fun addFavorite(@CurrentAccount account: Account, @RequestBody body: IntegerWrapper): RestResponse<Nothing?> {
        require(account is Student) {
            "Only student can add favorite dormitory."
        }

        val team =
            teamRepo.findTeamStudentLeads(account) ?: throw IllegalArgumentException("You are not leading any team.")
        val dormitory = dormitoryRepo.findById(body.id).orElseThrow { IllegalArgumentException("Dormitory not found.") }
        require(team.favorites.none { it.id == dormitory.id }) {
            "You have already added this dormitory."
        }
        require(dormitory.gender == team.gender) {
            "You can't select a dormitory which is different from your gender!"
        }

        team.favorites += dormitory
        teamRepo.save(team)

        return RestResponse.success(null, "Successfully added.")
    }

    @PostMapping("cancel-collect-dormitory")
    fun removeFavorite(@CurrentAccount account: Account, @RequestBody body: IntegerWrapper): RestResponse<Nothing?> {
        require(account is Student) {
            "Only student can remove favorite dormitory."
        }

        val team =
            teamRepo.findTeamStudentLeads(account) ?: throw IllegalArgumentException("You are not leading any team.")
        team.favorites.removeIf { it.id == body.id }
        teamRepo.save(team)

        return RestResponse.success(null, "Successfully removed.")
    }

    @PostMapping("select-dormitory")
    fun selectDormitory(@CurrentAccount account: Account, @RequestBody body: IntegerWrapper): RestResponse<Nothing?> {
        require(account is Student) {
            "Only student can select dormitory."
        }

        val team =
            teamRepo.findTeamStudentLeads(account) ?: throw IllegalArgumentException("You are not leading any team.")

        require(team.full) {
            "Your team is not full."
        }

        require(!teamRepo.existsByDormitoryId(body.id)) {
            "This dormitory is already been occupied!"
        }
        val dormitory = dormitoryRepo.findById(body.id).orElseThrow { IllegalArgumentException("Dormitory not found.") }
        require(team.dormitory?.id != dormitory.id) {
            "You have already selected this dormitory!"
        }
        require(dormitory.gender == team.gender) {
            "You can't select a dormitory which is different from your gender!"
        }

        team.dormitory = dormitory
        team.recruiting = false
        teamRepo.save(team)

        return RestResponse.success(null, "Successfully selected.")
    }
}
