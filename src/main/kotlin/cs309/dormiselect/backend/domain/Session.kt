package cs309.dormiselect.backend.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import java.sql.Timestamp
import java.util.*

@Entity
class Session(
    @OneToOne(optional = false) val account: Account,
    @Id val token: String = UUID.randomUUID().toString(),
    val expireTime: Timestamp = Timestamp(System.currentTimeMillis() + 1000 * 60 * 60 * 24)
) {
    constructor(account: Account, persistTime: Long) : this(
        account,
        expireTime = Timestamp(System.currentTimeMillis() + persistTime)
    )
}