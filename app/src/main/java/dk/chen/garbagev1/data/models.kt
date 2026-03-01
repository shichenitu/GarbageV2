package dk.chen.garbagev1.data

import dk.chen.garbagev1.domain.Item
import java.util.UUID

data class ItemDto(
    val id: String = UUID.randomUUID().toString(),
    val what: String,
    val where: String
)

fun ItemDto.toItem(): Item = Item(id = this.id, what = this.what, where = this.where)