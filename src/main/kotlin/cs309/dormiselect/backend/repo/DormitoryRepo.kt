package cs309.dormiselect.backend.repo

import cs309.dormiselect.backend.domain.Dormitory
import org.springframework.data.repository.CrudRepository

interface DormitoryRepo : CrudRepository<Dormitory, Int> {
    fun findByBuildingId(buildingId: String): List<Dormitory>

    //CrudRepository already has a function findById(id: ID): Optional<T>
    //fun findById(dormitoryId: String): Dormitory?
    fun findBySize(size: Int): List<Dormitory>
}