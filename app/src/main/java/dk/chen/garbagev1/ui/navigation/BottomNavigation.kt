package dk.chen.garbagev1.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import dk.chen.garbagev1.R
import dk.chen.garbagev1.ui.features.garbage.GarbageSearch
import dk.chen.garbagev1.ui.features.recycling.Bins
import dk.chen.garbagev1.ui.features.settings.Settings

interface AppRoute

interface NestedGraph : AppRoute {
    val startDestination: AppRoute
}

sealed class BottomNavigation(
    val route: AppRoute,
    val title: Int,
    val icon: ImageVector
) {
    data object GarbageTab : BottomNavigation(
        route = GarbageSearch,
        title = R.string.garbage_screen_title,
        icon = Icons.Default.RestoreFromTrash
    )

    data object BinsTab : BottomNavigation(
        route = Bins,
        title = R.string.bins_screen_title,
        icon = Icons.Default.Recycling
    )

    data object SettingsTab : BottomNavigation(
        route = Settings,
        title = R.string.settings_screen_title,
        icon = Icons.Default.Settings
    )
}