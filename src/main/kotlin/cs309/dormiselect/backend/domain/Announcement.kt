package cs309.dormiselect.backend.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.sql.Timestamp

@Entity
class Announcement(
    @ManyToOne(optional = false) val author: Account,
    var state: State,
    var content: String,
    var receiver: Receiver,
) {
    @Id
    @GeneratedValue
    val id: Int? = null
    val postTime: Timestamp = Timestamp(System.currentTimeMillis())
    enum class State {
        NORMAL, URGENT, EXTRA_URGENT
    }
    enum class Receiver{
        ADMINISTRATOR_ONLY, ADMIN_AND_TEACHER, ALL
    }
}