package cs309.dormiselect.backend.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.sql.Timestamp

@Entity
class Announcement(
    @ManyToOne(optional = false) val author: Account,
    var content: String,
    var receiver: Receiver = Receiver.ALL,
    var priority: Priority = Priority.NORMAL,
) {
    @Id
    @GeneratedValue
    val id: Int? = null
    val postTime: Timestamp = Timestamp(System.currentTimeMillis())

    enum class Priority {
        NORMAL, URGENT, EXTRA_URGENT
    }
    enum class Receiver{
        ADMINISTRATOR_ONLY, ADMIN_AND_TEACHER, ALL
    }
}