package by.bashlikovvv.music_player.tracks.ui.explorer.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import by.bashlikovvv.music_player.R
import by.bashlikovvv.music_player.tracks.domain.model.Track
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BrowserScreenMusicItem(
    track: Track,
    isCurrent: Boolean,
    onClick: (track: Track) -> Unit
) {
    val background = if (isCurrent) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.background
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(background)
            .clickable { onClick(track) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            model = track.image,
            contentDescription = "${track.fileName} image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(50.dp).padding(5.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
        ) {
            it
                .error(R.drawable.music_notes)
                .load(track.image)
        }
        Column (
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(0.8f)
        ) {
            Text(
                text = track.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = track.artist,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 5.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(0.2f)
        ) {
            Text(
                text = track.durationToStringFormat(),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            IconButton(onClick = {  }) {
                Image(
                    painter = painterResource(R.drawable.more_vert),
                    contentDescription = "tap to open more",
                    modifier = Modifier.size(25.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
                )
            }
        }
    }
}
