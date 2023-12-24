package cs309.dormiselect.backend.domain

import cs309.dormiselect.backend.domain.account.Account
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
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
    val likeNum: Int = 0

    // avatar is used to store an image
    val avatar: String? = null

    @OneToMany
    val replies: MutableList<Reply> = mutableListOf()
    val postTime: Timestamp = Timestamp(System.currentTimeMillis())
}