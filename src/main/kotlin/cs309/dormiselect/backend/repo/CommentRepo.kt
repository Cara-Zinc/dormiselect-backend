package cs309.dormiselect.backend.repo

import cs309.dormiselect.backend.domain.Account
import cs309.dormiselect.backend.domain.Comment
import cs309.dormiselect.backend.domain.Dormitory
import org.springframework.data.repository.CrudRepository
import java.sql.Timestamp

interface CommentRepo: CrudRepository<Comment, Int> {
    fun findByDormitory(dormitory: Dormitory): List<Comment>

    fun findByAuthor(account: Account): List<Comment>
    fun findByPostTime(postTimes: Timestamp): List<Comment>
}