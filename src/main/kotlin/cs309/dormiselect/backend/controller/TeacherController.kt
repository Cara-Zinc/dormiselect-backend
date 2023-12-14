package cs309.dormiselect.backend.controller

import cs309.dormiselect.backend.data.DormitoryDto
import cs309.dormiselect.backend.data.RestResponse
import cs309.dormiselect.backend.data.StudentInfoDto
import cs309.dormiselect.backend.domain.*
import cs309.dormiselect.backend.domain.account.Student
import cs309.dormiselect.backend.domain.account.Teacher
import cs309.dormiselect.backend.repo.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*


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
        val pageable: Pageable = PageRequest.of(page-1,pageSize)
        val resultPage: Page<Teacher> = teacherRepo.findAll(pageable)
        return RestResponse.success(resultPage.content, "Return teacher list page $pageSize")
    }

    @GetMapping("/team/list")
    fun viewTeamSelection(
        @RequestBody page: Int,
        @RequestBody pageSize: Int,
    ): RestResponse<List<Team>?> {
        val pageable: Pageable = PageRequest.of(page-1,pageSize)
        val resultPage: Page<Team> = teamRepo.findAll(pageable)
        return RestResponse.success(resultPage.content, "Return team list page $pageSize")
    }

    @GetMapping("/studentList")
    fun viewStudentList(
        @RequestBody page: Int,
        @RequestBody pageSize: Int,
    ): RestResponse<List<Student>?> {
        val pageable: Pageable = PageRequest.of(page - 1, pageSize)
        val resultPage: Page<Student> = studentRepo.findAll(pageable)
        return RestResponse.success(resultPage.content, "Nothing found")
    }

    @PostMapping("/dormitory/upload")
    fun uploadDormitory(
        @RequestBody dorm: DormitoryDto,
    ): RestResponse<Any?> {
        val roomExist = dormitoryRepo.existsByRoomIdAndZoneIdAndBuildingId(dorm.roomId, dorm.zoneId, dorm.buildingId)
        if (roomExist) {
            return RestResponse.fail(404, "The dormitory you upload already exist")
        } else {
            dormitoryRepo.newDormitory(dorm.roomId, dorm.zoneId, dorm.size, dorm.buildingId, dorm.info)
            return RestResponse.success(null, "upload dormitory successfully")
        }

    }

    @PostMapping("/student/upload")
    fun uploadStudent(
        @RequestBody studentInfoDto: StudentInfoDto
    ): RestResponse<Any?> {
        val studentExist = studentRepo.existsByStudentId(studentInfoDto.studentId)
        if (studentExist) {
            return RestResponse.fail(404, "The studentId you upload already exist")
        } else {
            accountRepo.newStudent(
                studentInfoDto.studentId,
                studentInfoDto.name,
                studentInfoDto.password,
                studentInfoDto.gender
            )
            return RestResponse.success(null, "upload student successfully")
        }

    }

    @PutMapping("/dormitory/edit")
    fun editDormitory(
        @RequestBody dormitoryDto: DormitoryDto
    ): RestResponse<Any?> {
        //TODO
        return RestResponse.success(null,"Edit Successfully")
    }

    @PutMapping("/student/edit")
    fun editStudentInfo(
        @RequestBody studentInfoDto: StudentInfoDto
    ): RestResponse<Any?> {

        return RestResponse.success(null,"Edit Successfully")
    }



}