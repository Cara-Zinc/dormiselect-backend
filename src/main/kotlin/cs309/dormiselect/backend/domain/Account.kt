package cs309.dormiselect.backend.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


@Entity
class Account(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: String,
    var password: String,
    var type: Type,

    ) {

    enum class Type {
        USER, ADMINISTRATOR
    }
}