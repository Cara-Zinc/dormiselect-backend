package cs309.dormiselect.backend.controller

import cs309.dormiselect.backend.data.RegisterRequestDto
import cs309.dormiselect.backend.data.RestResponse
import cs309.dormiselect.backend.domain.Account
import cs309.dormiselect.backend.domain.Administrator
import cs309.dormiselect.backend.domain.Student
import cs309.dormiselect.backend.repo.AccountRepo
import org.apache.commons.logging.LogFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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
    fun hello() = RestResponse.success("Hello, world!")

    @GetMapping("/error")
    fun error(): Nothing {
        throw ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "I'm a teapot.")
    }
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequestDto): ResponseEntity<String>{
        if(accountRepo.findById(request.id)!=null){
            return ResponseEntity.status(404).body("This ID already exist!")
        }
        if(accountRepo.findByName(request.username)!=null){
            return ResponseEntity.status(404).body("This username already exist!")
        }
        val account = when(request.type){
            "Student"-> Student(request.username,request.password,null)
            //TODO: handling logic of Teacher and Administrator account
            else -> {return ResponseEntity.status(404).body("Invalid account type!")}
        }


        return ResponseEntity.ok("Account register successfully")
    }
}