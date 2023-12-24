package cs309.dormiselect.backend.controller

import cs309.dormiselect.backend.data.*
import cs309.dormiselect.backend.data.dormitory.DormInfoDto
import cs309.dormiselect.backend.data.dormitory.DormListDto
import cs309.dormiselect.backend.data.dormitory.DormitoryDto
import cs309.dormiselect.backend.data.student.StudentInfoDto
import cs309.dormiselect.backend.data.student.StudentListDto
import cs309.dormiselect.backend.data.student.StudentUploadDto
import cs309.dormiselect.backend.domain.*
import cs309.dormiselect.backend.domain.account.Student
import cs309.dormiselect.backend.domain.account.Teacher
import cs309.dormiselect.backend.repo.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import kotlin.jvm.optionals.getOrElse


@RestController
@RequestMapping("/api/teacher")
class TeacherController(
    private val studentRepo: StudentRepo,
    private val dormitoryRepo: DormitoryRepo,
    private val accountRepo: AccountRepo,
    private val teamRepo: TeamRepo,
    private val teacherRepo: TeacherRepo,
) {
    @GetMapping("/list")
    fun teacherList(
        @RequestBody page: Int,
        @RequestBody pageSize: Int,
    ): RestResponse<List<Teacher>?> {
        val pageable: Pageable = PageRequest.of(page - 1, pageSize)
        val resultPage: Page<Teacher> = teacherRepo.findAll(pageable)
        return RestResponse.success(resultPage.content, "Return teacher list page $pageSize")
    }

    @GetMapping("/team/select/list")
    fun viewTeamSelection(
        @RequestBody page: Int,
        @RequestBody pageSize: Int,
    ): RestResponse<List<Team>?> {
        val pageable: Pageable = PageRequest.of(page - 1, pageSize)
        val resultPage: Page<Team> = teamRepo.findAll(pageable)
        return RestResponse.success(resultPage.content, "Return team list page $page")
    }

    @GetMapping("/student/list")
    fun viewStudentList(
        @RequestBody pageInfo: PageInfo,
    ): RestResponse<Any?> {
        val page = pageInfo.page
        val pageSize = pageInfo.pageSize
        if (page < 1 || pageSize < 1) {
            return RestResponse.fail(404,"Error: Page number should be positive and pageSize should be greater than 0")
        }

        val pageable: Pageable = PageRequest.of(page - 1, pageSize)
        val resultPage: Page<Student> = studentRepo.findAll(pageable)
        if (page > resultPage.totalPages) {
            return RestResponse.fail(404,"Error: Requested page number is too large")
        }
        val studentListDto = StudentListDto(
            total = resultPage.totalPages,
            page = page,
            pageSize = pageSize,
            rows = resultPage.content,

        )
        return RestResponse.success(studentListDto, "Return student list page $page")
    }

    @PostMapping("/dormitory/upload")
    fun uploadDormitory(
        @RequestBody dormList: List<DormInfoDto>,
    ): RestResponse<Any?> {
        var cnt: Int
        for(dorm in dormList){
            val roomExist = dormitoryRepo.existsByRoomIdAndZoneIdAndBuildingId(dorm.roomId, dorm.zoneId, dorm.buildingId)
            if (roomExist) {
                return RestResponse.fail(404, "The dormitory you upload already exist")
            }
        }
        for(dorm in dormList){
            dormitoryRepo.newDormitory(dorm.roomId, dorm.zoneId, dorm.size, dorm.buildingId, dorm.gender, dorm.info)
        }
        return RestResponse.success(null, "upload dormitory successfully")

    }

    @PostMapping("/student/upload")
    fun uploadStudent(
        @RequestBody studentUploadDtoList: List<StudentUploadDto>
    ): RestResponse<Any?> {
        for(studentInfoDto in studentUploadDtoList){
            val studentExist = studentRepo.existsByStudentId(studentInfoDto.studentId)
            if (studentExist) {
                return RestResponse.fail(404, "The studentId you upload already exist")
            }
        }
        for(studentInfoDto in studentUploadDtoList){
            val student = accountRepo.newStudent(
                studentInfoDto.studentId,
                studentInfoDto.name,
                studentInfoDto.password,
                studentInfoDto.gender
            )
            student.age = studentInfoDto.age
            student.department = studentInfoDto.department
            student.major = studentInfoDto.major

        }
        return RestResponse.success(null, "upload student successfully")

    }

    @PostMapping("/dormitory/edit")
    fun editDormitory(
        @RequestBody dormitoryDto: DormitoryDto
    ): RestResponse<Any?> {

        val dormitory = dormitoryRepo.findById(dormitoryDto.id)
            .getOrElse {
                return RestResponse.fail(404, "The dormitory you edit is not found in the database")
            }
        dormitory.apply {
            roomId = dormitoryDto.roomId
            zoneId = dormitoryDto.zoneId
            size = dormitoryDto.size
            buildingId = dormitoryDto.buildingId
            info = dormitoryDto.info
            comments.addAll(dormitoryDto.comments)
        }

        return RestResponse.success(null, "Edit dormitory info Successfully")
    }

    @PostMapping("/student/edit")
    fun editStudentInfo(
        @RequestBody studentInfoDto: StudentInfoDto
    ): RestResponse<Any?> {
        val student = studentRepo.findById(studentInfoDto.id)
            .getOrElse { return RestResponse.fail(404, "Can not find the student in the database") }
        /*
                studentInfoDto.bedTime?.let { student.bedTime = it }
                studentInfoDto.wakeUpTime?.let { student.wakeUpTime = it }
                studentInfoDto.email?.let { student.email = it }
                studentInfoDto.telephone?.let { student.telephone = it }
                studentInfoDto.department?.let { student.department = it }
                studentInfoDto.major?.let { student.major = it }
                studentInfoDto.qq?.let { student.qq = it }
                studentInfoDto.wechat?.let { student.wechat = it }
                studentInfoDto.age?.let { student.age = it }

         */
        student.bedTime = studentInfoDto.bedTime
        student.age = studentInfoDto.age
        student.qq = studentInfoDto.qq
        student.email = studentInfoDto.email
        student.department = studentInfoDto.department
        student.major = studentInfoDto.major
        student.wechat = studentInfoDto.wechat
        student.wakeUpTime = studentInfoDto.wakeUpTime
        student.telephone = studentInfoDto.telephone

        //TODO handle the logic of adding hobby into hobby list

        return RestResponse.success(null, "Edit student info Successfully")
    }

    @GetMapping("/dormitory/list")
    fun viewDormitoryList(
        @RequestBody page: Int,
        @RequestBody pageSize: Int,
    ): RestResponse<Any?> {
        if (page < 1 || pageSize < 1) {
            return RestResponse.fail(404,"Error: Page number should be positive and pageSize should be greater than 0")
        }

        val pageable: Pageable = PageRequest.of(page - 1, pageSize)
        val resultPage: Page<Dormitory> = dormitoryRepo.findAll(pageable)
        if (page > resultPage.totalPages) {
            return RestResponse.fail(404,"Error: Requested page number is too large")
        }
        val dormListDto = DormListDto(
            total = resultPage.totalPages,
            page = page,
            pageSize = pageSize,
            rows = resultPage.content,
        )
        return RestResponse.success(dormListDto, "Return dormitory list page $page")
    }

    @GetMapping("/student/information/form")
    fun viewStudentInfo(
        @RequestParam id: Int
    ): RestResponse<Any?> {
        val student = studentRepo.findById(id)
            .getOrElse { return RestResponse.fail(404, "Id not exist in the database") }
        val teamList = teamRepo.findByMembersIdContaining(id)
        var teamName: String? = null
        if (teamList.isNotEmpty()) {
            teamName = teamList[0].name
        }
        val studentInfoDto = StudentInfoDto(
            student.id!!,
            student.studentId,
            student.name,
            student.password,
            student.gender,
            student.bedTime,
            student.wakeUpTime,
            student.email,
            student.telephone,
            student.department,
            student.major,
            student.qq,
            student.wechat,
            student.age,
            teamName,
            student.hobbies,
        )
        return RestResponse.success(studentInfoDto, "check for student $id 's information")
    }

    @PostMapping("/team/remove_member")
    fun removeTeamMember(
        @RequestBody teamId: Int,
        @RequestBody studentId: Int,
    ):RestResponse<Any?>{
        val team = teamRepo.findById(teamId).getOrElse { return RestResponse.fail(404,"Team $teamId does not exist") }
        val teamFind = teamRepo.findTeamStudentBelongTo(studentId)
            ?: return RestResponse.fail(404,"Student $studentId does not join any team!")
        if (teamFind.id==team.id){
            team.members.removeIf { student -> student.id == studentId }
            teamRepo.save(team)
            return RestResponse.success(null,"Delete successfully")
        }
        else{
            return RestResponse.fail(404,"Student $studentId does not belong to ${team.name}")
        }
    }

}