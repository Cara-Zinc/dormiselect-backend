package cs309.dormiselect.backend.domain

import jakarta.persistence.*

@Entity
class Teacher(
    name: String,
    password: String,
) : Account(name, password) {
    var buildingInCharge: String? = null
}