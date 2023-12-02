package cs309.dormiselect.backend.repo

import cs309.dormiselect.backend.domain.Announcement
import org.springframework.data.repository.CrudRepository

interface AnnouncementRepo: CrudRepository<Announcement,Int> {

}