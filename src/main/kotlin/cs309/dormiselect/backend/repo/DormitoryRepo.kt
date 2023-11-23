package cs309.dormiselect.backend.repo

import cs309.dormiselect.backend.domain.Dormitory
import jakarta.persistence.Id
import org.springframework.data.repository.CrudRepository

interface DormitoryRepo: CrudRepository<Dormitory, Int> {
    fun findByBuildingId(buildingId: String): List<Dormitory>
    fun findById(dormitoryId: String): Dormitory?
    fun findBySize(size: Int): List<Dormitory>
}