package cs309.dormiselect.backend.controller

import cs309.dormiselect.backend.config.CurrentAccount
import cs309.dormiselect.backend.data.PageInfo
import cs309.dormiselect.backend.data.PageInformation
import cs309.dormiselect.backend.data.RestResponse
import cs309.dormiselect.backend.data.asRestResponse
import cs309.dormiselect.backend.data.student.StudentInfoDto
import cs309.dormiselect.backend.data.student.StudentListDto
import cs309.dormiselect.backend.domain.Announcement
import cs309.dormiselect.backend.domain.Comment
import cs309.dormiselect.backend.domain.Dormitory
import cs309.dormiselect.backend.domain.account.Account
import cs309.dormiselect.backend.domain.account.Student
import cs309.dormiselect.backend.repo.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
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

    @GetMapping("/dormitory/list")
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
        //TODO commentDto and logic of adding comments
        commentRepo.save(comment)
        return RestResponse.success(null, "Post comment successfully")
    }

    @GetMapping("/announcement")
    fun viewAnnouncement(): RestResponse<List<Announcement>?> {
        return announcementRepo.findAll().toList().asRestResponse()
    }

    @GetMapping("/announcement/list")
    fun viewAnnouncement(@RequestBody body: PageInformation): RestResponse<Any?> {
        val page = PageRequest.of(body.page, body.pageSize)
        val result = announcementRepo.findAll(page)
        return object {
            val total = result.totalPages
            val page = body.page
            val pageSize = body.pageSize
            val rows = result.content
        }.asRestResponse()
    }

    @GetMapping("/information/post")
    fun viewStudentInfo(@CurrentAccount account: Account):RestResponse<Any?>{
        account.id?:return RestResponse.fail(404,"You haven't login yet")
        val student = studentRepo.findById(account.id!!)
            .getOrElse { return RestResponse.fail(404, "This id is not in the database")}
        return student.asRestResponse("The student information is found!")
    }

    @PostMapping("/information/post")
    fun editStudentInfo(
        @CurrentAccount account: Account,
        @RequestBody studentInfoDto: StudentInfoDto,
    ):RestResponse<Any?>{
        account.id?:return RestResponse.fail(0,"You have not login yet")
        if(account.id!=studentInfoDto.id){
            return RestResponse.fail(401,"Your login account is different from the account you want to edit")
        }
        val student = studentRepo.findById(account.id!!)
            .getOrElse {return RestResponse.fail(404,"The id is not in the database") }
        student.apply{
            bedTime = studentInfoDto.bedTime
            age = studentInfoDto.age
            qq = studentInfoDto.qq
            email = studentInfoDto.email
            department = studentInfoDto.department
            major = studentInfoDto.major
            wechat = studentInfoDto.wechat
            wakeUpTime = studentInfoDto.wakeUpTime
            telephone = studentInfoDto.telephone
            hobbies.clear()
            hobbies += studentInfoDto.hobbies
        }
        studentRepo.save(student)
        return RestResponse.success(null,"Successfully edit!")

    }

    @GetMapping("/student/list")
    fun viewStudentList(
        @RequestBody pageInfo: PageInfo,
    ): RestResponse<Any?> {
        val page = pageInfo.page
        val pageSize = pageInfo.pageSize
        if (page < 1 || pageSize < 1) {
            return RestResponse.fail(404, "Error: Page number should be positive and pageSize should be greater than 0")
        }

        val pageable: Pageable = PageRequest.of(page - 1, pageSize)
        val resultPage: Page<Student> = studentRepo.findAll(pageable)
        if (page > resultPage.totalPages) {
            return RestResponse.fail(404, "Error: Requested page number is too large")
        }
        val studentListDto = StudentListDto(
            total = resultPage.totalPages,
            page = page,
            pageSize = pageSize,
            rows = resultPage.content,

            )
        return RestResponse.success(studentListDto, "Return student list page $page")
    }
}





