package cs309.dormiselect.backend.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Dormitory(
    var zoneId: String,
    var size: Int,
    var buildingId: String,
    var info: String = "",
) {
    @Id
    @GeneratedValue
    var id: Int? = null
        protected set

    @OneToMany(orphanRemoval = true)
    val comments: MutableList<Comment> = mutableListOf()
}