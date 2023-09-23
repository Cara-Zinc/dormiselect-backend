package cs309.dormiselect.backend.domain

import jakarta.persistence.*

@Entity
class Domitory(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: String,
    var zoneId: String,
    var size: Int,
    var buildingId: String,
    @OneToMany val comments: MutableList<Comments>
)