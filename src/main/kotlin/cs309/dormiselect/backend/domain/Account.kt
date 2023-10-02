package cs309.dormiselect.backend.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToOne


@Entity
class Account(
    @Id @GeneratedValue val id: Int,
    var name: String,
    var password: String,
    var type: Type = Type.USER,
    @OneToOne(orphanRemoval = true) var student: Student? = null
) {

    enum class Type {
        USER, ADMINISTRATOR
    }
}