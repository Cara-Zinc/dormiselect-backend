package cs309.dormiselect.backend.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.sql.Timestamp

@Entity
class Comment(
    @ManyToOne(optional = false) val dormitory: Dormitory,
    @ManyToOne(optional = false) val author: Account,
    var content: String,
) {
    @Id
    @GeneratedValue
    val id: Int? = null

    val postTime: Timestamp = Timestamp(System.currentTimeMillis())
}