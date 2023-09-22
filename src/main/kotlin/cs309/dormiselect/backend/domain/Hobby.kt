package cs309.dormiselect.backend.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Hobby(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var studnetId: String,
    var hobby: String
) {

}