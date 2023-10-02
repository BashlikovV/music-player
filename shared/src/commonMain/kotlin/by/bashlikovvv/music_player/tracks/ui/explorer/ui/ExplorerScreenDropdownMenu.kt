package by.bashlikovvv.music_player.tracks.ui.explorer.ui

import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import by.bashlikovvv.music_player.tracks.ui.explorer.ExplorerComponent

@Composable
fun BrowserScreenDropdownMenu(
    component: ExplorerComponent,
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {
    val view = LocalView.current

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest() },
        offset = DpOffset(
            x = view.width.dp,
            y = 0.dp
        )
    ) {
        DropdownMenuItem(
            onClick = { component.onOpenSettingsScreen() }
        ) {
            Text("Settings")
        }
    }
}