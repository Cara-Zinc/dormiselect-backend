package cs309.dormiselect.backend.domain

import jakarta.persistence.Entity

@Entity
class Administrator(name: String, password: String) : Account(name, password) {
}