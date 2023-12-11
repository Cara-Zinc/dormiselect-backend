package cs309.dormiselect.backend.repo

import cs309.dormiselect.backend.domain.Dormitory
import org.springframework.data.repository.CrudRepository

interface DormitoryRepo : CrudRepository<Dormitory, Int> {
    fun findByBuildingId(buildingId: Int): List<Dormitory>

    //CrudRepository already has a function findById(id: ID): Optional<T>
    //fun findById(dormitoryId: String): Dormitory?
    fun findBySize(size: Int): List<Dormitory>
    fun existsByRoomIdAndZoneIdAndBuildingId(roomId: Int, zoneId: Int, buildingId: Int): Boolean

}

fun DormitoryRepo.newDormitory(roomId: Int, zoneId: Int, size: Int, buildingId: Int, info: String) =
    Dormitory(roomId, zoneId, size, buildingId, info).also { save(it) }
