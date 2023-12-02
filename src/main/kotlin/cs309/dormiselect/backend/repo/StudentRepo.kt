package cs309.dormiselect.backend.repo

import cs309.dormiselect.backend.domain.Student
import org.springframework.data.repository.CrudRepository

interface StudentRepo : CrudRepository<Student, Int> {
    fun findByName(name: String): Student
}