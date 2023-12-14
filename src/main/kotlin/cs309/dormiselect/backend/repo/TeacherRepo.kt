package cs309.dormiselect.backend.repo

import cs309.dormiselect.backend.domain.account.Teacher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface TeacherRepo : CrudRepository<Int, Teacher> {
    fun findAll(pageable: Pageable): Page<Teacher>
}