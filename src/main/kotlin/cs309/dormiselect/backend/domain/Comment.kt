package cs309.dormiselect.backend.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.sql.Timestamp

@Entity
class Comment(
    @Id @GeneratedValue val id: Int,
    var postTime: Timestamp,
    @ManyToOne(optional = false) val author: Account,
    var content: String,
    @ManyToOne(optional = false) val domitory: Domitory
)