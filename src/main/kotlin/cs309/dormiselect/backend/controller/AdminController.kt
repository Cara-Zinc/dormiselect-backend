package cs309.dormiselect.backend.controller

import cs309.dormiselect.backend.data.PageInfo
import cs309.dormiselect.backend.data.RestResponse
import cs309.dormiselect.backend.data.teacher.TeacherUploadDto
import cs309.dormiselect.backend.domain.account.Teacher
import cs309.dormiselect.backend.repo.TeacherRepo
import cs309.dormiselect.backend.repo.newTeacher
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/administrator")
class AdminController(
    private val teacherRepo: TeacherRepo,
) {
    @GetMapping("/teacher/list")
    fun viewTeacherList(
        @RequestBody pageInfo: PageInfo,
    ): RestResponse<List<Teacher>?> {
        val pageable: Pageable = PageRequest.of(pageInfo.page - 1, pageInfo.pageSize)
        val resultPage: Page<Teacher> = teacherRepo.findAll(pageable)
        return RestResponse.success(resultPage.content, "Return teacher list page ${pageInfo.pageSize}")
    }

    @PostMapping("/teacher/upload")
    fun uploadTeacher(
        @RequestBody teacherInfoList: List<TeacherUploadDto>
    ): RestResponse<Any?> {
        for (teacherInfo in teacherInfoList) {
            if (teacherRepo.existsByTeacherId(teacherInfo.teacherId)) {
                return RestResponse.fail(404, "The teacher you upload already exists")
            }
        }

        for (teacherInfo in teacherInfoList) {
            teacherRepo.newTeacher(teacherInfo.teacherId, teacherInfo.name, teacherInfo.password)
        }
        return RestResponse.success(null, "Successfully upload teacher accounts")
    }

}