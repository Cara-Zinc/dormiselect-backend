package cs309.dormiselect.backend.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.sql.Timestamp

@Entity
class Post (
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var postId: String,
    var postTime: Timestamp,
    var authorId: String,
    var content: String,
    var commentRoomId: String,

){
}