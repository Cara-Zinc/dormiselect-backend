package cs309.dormiselect.backend.controller

import cs309.dormiselect.backend.config.CurrentAccount
import cs309.dormiselect.backend.data.RestResponse
import cs309.dormiselect.backend.domain.Account
import cs309.dormiselect.backend.domain.Administrator
import cs309.dormiselect.backend.repo.AccountRepo
import org.apache.commons.logging.LogFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api/account")
class AccountController(val accountRepo: AccountRepo) {
    private val logger = LogFactory.getLog(javaClass)

    private fun tryInitRootAccount() {
        if (accountRepo.findAll().any { it is Administrator }) {
            return
        }

        val password = UUID.randomUUID().toString()

        val account = Administrator("root", password)
        accountRepo.save(account)

        logger.info("No admin account is found so a new one is created.")
        logger.info("Username: ${account.name}, Password: $password")
    }

    init {
        tryInitRootAccount()
    }

    @GetMapping("/hello")
    fun hello(@CurrentAccount account: Account) = RestResponse.success("Hello, ${account.name}!")

    @GetMapping("/error")
    fun error(): Nothing {
        throw ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "I'm a teapot.")
    }
}