package by.bashlikovvv.music_player.tracks.ui.explorer.ui.modal_bottom_sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.layoutId

@Composable
fun ModalBottomSheetTopBar() {
    Row(
        modifier = Modifier.fillMaxWidth().height(50.dp).layoutId("sheetTopBar"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

    }
}