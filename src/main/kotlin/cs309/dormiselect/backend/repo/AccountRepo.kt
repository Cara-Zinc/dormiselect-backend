package cs309.dormiselect.backend.repo

import cs309.dormiselect.backend.domain.Account
import cs309.dormiselect.backend.domain.Administrator
import cs309.dormiselect.backend.domain.Student
import cs309.dormiselect.backend.domain.Teacher
import org.springframework.data.repository.CrudRepository

interface AccountRepo : CrudRepository<Account, Int> {
    fun findByName(name: String): Account?

    //No! Don't! ID is Int, not String!
    //fun findById(id: String): Account?
    fun findByNameAndPassword(name: String, password: String): Account?
}

fun AccountRepo.newStudent(
    studentId: Long, name: String, password: String, gender: Student.Gender
): Student {
    return Student(studentId, name, password, gender).also { save(it) }
}

fun AccountRepo.newTeacher(
    name: String,
    password: String,

): Teacher {
    return Teacher(name, password).also { save(it) }
}

fun AccountRepo.newAdministrator(name: String, password: String): Administrator {
    return Administrator(name, password).also { save(it) }
}