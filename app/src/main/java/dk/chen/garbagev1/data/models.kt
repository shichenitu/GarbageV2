package dk.chen.garbagev1.data

import androidx.compose.ui.graphics.Color
import dk.chen.garbagev1.domain.Item
import dk.chen.garbagev1.domain.Bin
import java.util.UUID
import androidx.core.graphics.toColorInt
import dk.chen.garbagev1.data.database.ItemEntity
import dk.chen.garbagev1.data.database.BinEntity

data class ItemDto(
    val id: String = UUID.randomUUID().toString(),
    val what: String,
    val where: String
)

data class BinDto(
    val name: String,
    val imageUrl: String,
    val binColor: String
)

fun BinDto.toBin(): Bin = Bin(
    name = this.name, imageUrl = this.imageUrl, binColor = Color(
        color = this.binColor.toColorInt()
    )
)

fun ItemDto.toItem(): Item = Item(
    id = this.id,
    what = this.what,
    where = this.where
)

fun ItemEntity.toItemDto() = ItemDto(
    id = id,
    what = what,
    where = where
)

fun BinEntity.toBinDto() = BinDto(
    name = name,
    imageUrl = imageUrl,
    binColor = binColor
)