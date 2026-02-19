package dk.chen.garbagev1.ui.features

import android.R.attr.visible
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.chen.garbagev1.R
import dk.chen.garbagev1.domain.Item
import dk.chen.garbagev1.domain.ItemRepository
import dk.chen.garbagev1.ui.components.SnackBarHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class GarbageSortingViewModel @Inject constructor (
    itemRepository: ItemRepository,
    snackBarHandler: SnackBarHandler
) : ViewModel() {
    private val sortingListVisibility: MutableStateFlow<Boolean> = MutableStateFlow(value = false)
    private val sortingList: StateFlow<List<Item>> =
        itemRepository.getSortingList()
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = emptyList()
            )

    private val itemWhat: MutableStateFlow<String> = MutableStateFlow(value = "")
    private val itemWhere: MutableStateFlow<String> = MutableStateFlow(value = "")

    val uiState: StateFlow<UiState> = combine(
        flow = sortingList,
        flow2 = sortingListVisibility,
        flow3 = itemWhat,
        flow4 = itemWhere
    ) { currentShoppingList, currentListVisibility, what, where ->
        UiState(
            shoppingList = currentShoppingList,
            displayShoppingList = currentListVisibility,
            itemWhat = what,
            itemWhere = where,
            toggleListVisibilityButtonLabel = if (currentListVisibility) R.string.search_item_label else R.string.show_sorting_list_label
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(stopTimeoutMillis = 500),
        initialValue = UiState()
    )

    val uiEvents = object : UiEvents {
        override fun onWhatChange(newValue: String) {
            itemWhat.update { newValue }
        }

        override fun onWhereChange(newValue: String) {
            itemWhere.update { newValue }
        }

        override fun onAddItemClick(
            itemWhat: String,
            itemWhere: String,
        ) {
            if (itemWhat.isNotBlank() && itemWhere.isNotBlank()) {
                itemRepository.addItem(Item(what = itemWhat.trim(), where = itemWhere.trim()))
                this@GarbageSortingViewModel.itemWhat.update { "" }
                this@GarbageSortingViewModel.itemWhere.update { "" }
            } else {
                snackBarHandler.postMessage(msgRes = R.string.textfield_error_message)
            }
        }

        override fun onRemoveItemClick(item: Item) {
            itemRepository.removeItem(item)
            snackBarHandler.postMessage(
                msgRes = R.string.item_removed_label,
                item.what,
                item.where,
                actionLabelRes = R.string.undo_label,
                onDismiss = { },
                onActionClick = {
                    itemRepository.addItem(item)
                    snackBarHandler.postMessage(msgRes = R.string.undo_confirmation_message)
                }
            )
        }

        override fun onToggleListVisibilityClick() {
            sortingListVisibility.update { !it }
        }
    }

    data class UiState(
        val shoppingList: List<Item> = emptyList(),
        val displayShoppingList: Boolean = false,
        val itemWhat: String = "",
        val itemWhere: String = "",
        @get:StringRes val toggleListVisibilityButtonLabel: Int = R.string.show_sorting_list_label
    )

    @Immutable
    interface UiEvents {
        fun onAddItemClick(itemWhat: String, itemWhere: String)
        fun onRemoveItemClick(item: Item)
        fun onToggleListVisibilityClick()
        fun onWhatChange(newValue: String)
        fun onWhereChange(newValue: String)
    }
}