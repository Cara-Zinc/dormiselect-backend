package cs309.dormiselect.backend.repo

import cs309.dormiselect.backend.domain.Account
import org.springframework.data.repository.CrudRepository

interface AccountRepo : CrudRepository<Account, Int> {
    fun findByName(name: String): Account?

    fun findByNameAndPassword(name: String, password: String): Account?
}