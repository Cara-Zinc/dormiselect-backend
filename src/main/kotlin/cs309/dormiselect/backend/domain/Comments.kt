package cs309.dormiselect.backend.domain

import jakarta.persistence.*
import java.sql.Timestamp

@Entity
class Comments(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: String,
    var postTime: Timestamp,
    @OneToOne val author: Account,
    var content: String,
    @OneToOne val domitory: Domitory
)