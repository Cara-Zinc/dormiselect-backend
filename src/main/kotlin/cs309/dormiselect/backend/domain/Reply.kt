package cs309.dormiselect.backend.domain

import cs309.dormiselect.backend.domain.account.Account
import jakarta.persistence.*
import java.sql.Timestamp

@Entity
class Reply(
    @ManyToOne(optional = false) val comment: Comment,
    @ManyToOne(optional = false) val author: Account,
    var content: String
) {
    @Id
    @GeneratedValue
    val id: Int? = null
    var likeNum: Int = 0
    val postTime: Timestamp = Timestamp(System.currentTimeMillis())

    fun like() {
        likeNum++
    }
}