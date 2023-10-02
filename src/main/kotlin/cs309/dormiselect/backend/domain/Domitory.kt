package cs309.dormiselect.backend.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Domitory(
    @Id @GeneratedValue val id: Int,
    var zoneId: String,
    var size: Int,
    var buildingId: String,
    var info: String = "",
    @OneToMany(orphanRemoval = true) val comments: MutableList<Comment> = mutableListOf()
)