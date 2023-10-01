package by.bashlikovvv.music_player.tracks.ui.browser.ui

import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import by.bashlikovvv.music_player.tracks.ui.browser.ExplorerComponent

@Composable
fun BrowserScreenDropdownMenu(
    component: ExplorerComponent,
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest() },
        offset = DpOffset(
            x = 0.dp,
            y = 50.dp
        )
    ) {
        DropdownMenuItem(
            onClick = { component.onOpenSettingsScreen() }
        ) {
            Text("Settings")
        }
    }
}