package by.bashlikovvv.music_player.tracks.ui.browser.ui.modal_bottom_sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.layoutId
import by.bashlikovvv.music_player.tracks.ui.browser.ExplorerComponent
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun ModalBottomSheetMusicIndicators(component: ExplorerComponent) {
    val state by component.state.collectAsState()
    val valueRange = 0f..180f
    var value = state.currentTime
    val minutes = value / 60f
    val seconds = round(value % 60)

    Column(
        modifier = Modifier.fillMaxWidth().layoutId("musicIndicators"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Slider(
                value = value,
                onValueChange = { value = it },
                valueRange = valueRange
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text("${minutes.roundToInt()}:${seconds}")
            Text(valueRange.endInclusive.toString())
        }
    }
}