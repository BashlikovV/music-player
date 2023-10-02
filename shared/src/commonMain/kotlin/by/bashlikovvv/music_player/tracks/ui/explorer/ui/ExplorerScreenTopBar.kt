package by.bashlikovvv.music_player.tracks.ui.explorer.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import by.bashlikovvv.music_player.R

@Composable
fun BrowserScreenTopBar(
    onOpenDropdownMenu: () -> Unit
) {
    var isSearchClicked by rememberSaveable { mutableStateOf(false) }
    val searchContentFraction by animateFloatAsState(
        if (isSearchClicked) {
            0.85f
        } else {
            0f
        }, label = ""
    )
    var textFieldValue by rememberSaveable { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth().height(65.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Player",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(start = 5.dp)
        )
        Row {
            TextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                modifier = Modifier.fillMaxWidth(searchContentFraction),
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                singleLine = true
            )
            IconButton(onClick = { isSearchClicked = !isSearchClicked }) {
                Icon(
                    painter = painterResource(R.drawable.search),
                    contentDescription = "tap to search tracks",
                    modifier = Modifier.size(25.dp)
                )
            }
            AnimatedVisibility(!isSearchClicked) {
                Row {
                    IconButton(onClick = {  }) {
                        Icon(
                            painter = painterResource(R.drawable.queue_music),
                            contentDescription = "open playlists"
                        )
                    }
                    IconButton(onClick = { onOpenDropdownMenu() }) {
                        Icon(
                            painter = painterResource(R.drawable.menu),
                            contentDescription = "tap to open menu",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            }
        }
    }
}