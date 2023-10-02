package by.bashlikovvv.music_player.tracks.ui.explorer.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import by.bashlikovvv.music_player.R
import by.bashlikovvv.music_player.tracks.ui.explorer.ExplorerComponent

@Composable
fun ExplorerScreenTracksInfo(component: ExplorerComponent) {
    var expanded by remember { mutableStateOf(false) }
    val state by component.state.collectAsState()

    Box {
        SortingTypesMenu(expanded) { expanded = false }
        Column {
            Spacer(
                modifier = Modifier
                    .border(BorderStroke(0.1.dp, MaterialTheme.colorScheme.secondary))
                    .fillMaxWidth()
                    .height(0.2.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth().height(50.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.padding(start = 5.dp)) {
                    Text("${state.tracks.size} tracks")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {  }) {
                        Icon(
                            painter = painterResource(R.drawable.shuffle),
                            contentDescription = "shuffle tracks",
                        )
                    }
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            painter = painterResource(R.drawable.sort),
                            contentDescription = "select sorting type"
                        )
                    }
                }
            }
            Spacer(
                modifier = Modifier
                    .border(BorderStroke(0.1.dp, MaterialTheme.colorScheme.secondary))
                    .fillMaxWidth()
                    .height(0.2.dp)
            )
        }
    }
}

@Composable
private fun SortingTypesMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {
    val view = LocalView.current

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        offset = DpOffset(x = view.width.dp, y = 0.dp)
    ) {
        DropdownMenuItem(onClick = {  }) {
            Text("By default")
        }
        DropdownMenuItem(onClick = {  }) {
            Text("By name")
        }
        DropdownMenuItem(onClick = {  }) {
            Text("By musician")
        }
        DropdownMenuItem(onClick = {  }) {
            Text("By duration")
        }
        DropdownMenuItem(onClick = {  }) {
            Text("By creating date")
        }
    }
}