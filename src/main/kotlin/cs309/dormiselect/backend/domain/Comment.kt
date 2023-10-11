package cs309.dormiselect.backend.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.sql.Timestamp

@Entity
class Comment(
    @ManyToOne(optional = false) val author: Account,
    var content: String,
    @ManyToOne(optional = false) val domitory: Domitory,
    var postTime: Timestamp = Timestamp(System.currentTimeMillis()),
    @Id @GeneratedValue var id: Int? = null,
)