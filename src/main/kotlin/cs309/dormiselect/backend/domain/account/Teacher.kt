package cs309.dormiselect.backend.domain.account

import jakarta.persistence.Entity

@Entity
class Teacher(
    name: String,
    password: String,
) : Account(name, password) {
    var buildingInCharge: String? = null
}