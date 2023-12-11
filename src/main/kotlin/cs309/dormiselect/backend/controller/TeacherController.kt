package cs309.dormiselect.backend.controller

import cs309.dormiselect.backend.config.CurrentAccount
import cs309.dormiselect.backend.data.DormitoryDto
import cs309.dormiselect.backend.data.RestResponse
import cs309.dormiselect.backend.domain.*
import cs309.dormiselect.backend.domain.account.Account
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
) {
    @GetMapping("/list")
    fun teacherList(
        @RequestBody page: Int,
        @RequestBody pageSize: Int,
    ): RestResponse<List<Teacher>?> {
        //TODO
        return RestResponse.success(null, "Success!")
    }

    @GetMapping("/team/list")
    fun viewTeamSelection(
        @RequestBody page: Int,
        @RequestBody pageSize: Int,
    ): RestResponse<List<Team>?> {


        return RestResponse.success(null, "Nothing found")
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
        @CurrentAccount account: Account,
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


}