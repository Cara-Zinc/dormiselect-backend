package cs309.dormiselect.backend.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToOne


@Entity
class Account(
    var name: String,
    var password: String,
    val type: Type = Type.USER,
    @OneToOne(orphanRemoval = true) val student: Student? = null,
    @Id @GeneratedValue var id: Int? = null
) {

    enum class Type {
        USER, ADMINISTRATOR
    }
}