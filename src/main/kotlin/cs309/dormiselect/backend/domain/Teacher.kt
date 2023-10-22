package cs309.dormiselect.backend.domain

import jakarta.persistence.*

@Entity
class Teacher(
    name: String,
    password: String,
    var buildingInCharge: String,
) : Account(name, password) {


}