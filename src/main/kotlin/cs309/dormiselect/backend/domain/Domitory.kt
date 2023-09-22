package cs309.dormiselect.backend.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Domitory (
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var dormId: String,
    var zoneId: String,
    var size: Int,
    var buildingId: String,
    var description: String
){

}