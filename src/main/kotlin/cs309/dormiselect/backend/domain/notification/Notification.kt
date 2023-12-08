package cs309.dormiselect.backend.domain.notification

import cs309.dormiselect.backend.domain.account.Account
import jakarta.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
class Notification(
    @ManyToOne val receiver: Account,
    val message: String,
) {
    @Id
    @GeneratedValue
    val id: Long? = null

    var valid = true
    var read = false
    var done = false
        set(value) {
            if (value) {
                read = true
            }
            field = value
        }
}