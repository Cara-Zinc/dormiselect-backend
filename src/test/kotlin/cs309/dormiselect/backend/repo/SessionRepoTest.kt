package cs309.dormiselect.backend.repo

import cs309.dormiselect.backend.domain.Administrator
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import kotlin.test.assertEquals
import kotlin.test.assertNull

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SessionRepoTest(
    @Autowired val accountRepo: AccountRepo,
    @Autowired val sessionRepo: SessionRepo,
) {
    @Test
    fun testSession() {
        val admin = Administrator("admin", "1919810")
        accountRepo.save(admin)

        val session = sessionRepo.createSession(admin, 1000)

        Thread.sleep(500)
        val sessionAccount = sessionRepo.findSession(session.token)
        assertEquals(admin, sessionAccount, "Token is expired.")
        println("Found session account: $sessionAccount")

        Thread.sleep(1000)
        assertNull(sessionRepo.findSession(session.token), "Token is not expired.")
        println("Session is expired.")
    }
}