package cs309.dormiselect.backend.domain

import jakarta.persistence.*


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Account(
    var name: String,
    var password: String,
) {
    @Id
    @GeneratedValue
    var id: Int? = null
        protected set
}