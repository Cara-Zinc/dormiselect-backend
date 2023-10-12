package cs309.dormiselect.backend.repo

import cs309.dormiselect.backend.domain.Account
import cs309.dormiselect.backend.domain.Session
import org.springframework.data.repository.CrudRepository
import java.sql.Timestamp
import kotlin.jvm.optionals.getOrNull

interface SessionRepo : CrudRepository<Session, String>

fun SessionRepo.findSession(token: String): Account? {
    val session = findById(token).getOrNull() ?: return null
    if (session.expireTime < Timestamp(System.currentTimeMillis())) {
        delete(session)
        return null
    }
    return session.account
}

fun SessionRepo.createSession(account: Account, persistTime: Long): Session {
    val session = Session(account, persistTime)
    save(session)
    return session
}