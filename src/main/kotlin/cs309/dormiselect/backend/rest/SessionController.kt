package cs309.dormiselect.backend.rest

import cs309.dormiselect.backend.repo.AccountRepo
import cs309.dormiselect.backend.repo.SessionRepo
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/session")
class SessionController(
    val sessionRepo: SessionRepo,
    val accountRepo: AccountRepo
) {
    @PostMapping("/login")
    fun login(@RequestBody name: String, @RequestBody password: String, response: HttpServletResponse) {
        val account = accountRepo.findByNameAndPassword(name, password) ?: throw ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "Invalid username or password."
        )

        val maxAge = 60 * 60 * 24
        val session = sessionRepo.createSession(account, maxAge * 1000L)
        val cookie = Cookie("token", session.token)
        cookie.maxAge = maxAge
        response.addCookie(cookie)
    }
}