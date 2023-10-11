package cs309.dormiselect.backend.repo

import cs309.dormiselect.backend.domain.Account
import cs309.dormiselect.backend.domain.Student
import org.springframework.data.repository.CrudRepository

interface AccountRepo : CrudRepository<Account, Int> {
    fun newAccount(name: String, password: String, student: Student? = null): Account {
        val account =
            Account(name, password, if (student == null) Account.Type.ADMINISTRATOR else Account.Type.USER, student)
        save(account)
        return account
    }

    fun findByNameAndPassword(name: String, password: String): Account?
}