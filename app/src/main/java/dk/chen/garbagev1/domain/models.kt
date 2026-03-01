package dk.chen.garbagev1.domain

import dk.chen.garbagev1.data.ItemDto
import java.util.UUID

data class Item(
    val id: String = UUID.randomUUID().toString(),
    val what: String,
    val where: String
)

fun Item.toDto(): ItemDto = ItemDto(id = this.id, what = this.what, where = this.where)

fun Item.fullDescription(): String = "${this.what.lowercase()} should be placed in: ${this.where}"