package cs309.dormiselect.backend.repo

import cs309.dormiselect.backend.domain.Administrator
import cs309.dormiselect.backend.domain.Student
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.sql.Timestamp

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepoTest(
    @Autowired val accountRepo: AccountRepo,
) {

    @Test
    fun testInsert() {
        val student = Student("student", "114514", Student.Gender.MALE)
        student.hobbies += setOf("eat", "drink", "play", "sleep")
        student.bedTime = Timestamp.valueOf("2021-01-01 23:00:00")
        student.wakeUpTime = Timestamp.valueOf("2021-01-02 07:00:00")
        val admin = Administrator("admin", "1919810")

        accountRepo.save(student)
        accountRepo.save(admin)

        val savedStudent = accountRepo.findByNameAndPassword("student", "114514")
        val savedAdmin = accountRepo.findByNameAndPassword("admin", "1919810")

        assert(savedStudent != null && savedStudent is Student)
        assert(savedAdmin != null && savedAdmin is Administrator)

        with(savedStudent as Student) {
            println("I'm a student who likes ${hobbies.joinToString()}, sleeps at $bedTime and wakes up at $wakeUpTime")
            println("My name is $name and my password is $password")
        }

        with(savedAdmin as Administrator) {
            println("I'm an admin.")
            println("My name is $name and my password is $password")
        }
    }
}