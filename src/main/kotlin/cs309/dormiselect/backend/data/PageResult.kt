package cs309.dormiselect.backend.data

import org.springframework.data.domain.Page

data class PageResult<out T>(
    val total: Int,
    val page: Int,
    val pageSize: Int,
    val rows: List<T>
)

fun <T> Page<T>.toPageResult(): PageResult<T> {
    return PageResult(totalPages, number, size, content)
}
