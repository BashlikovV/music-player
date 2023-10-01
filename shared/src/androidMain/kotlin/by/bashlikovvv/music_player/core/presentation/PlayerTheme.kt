package by.bashlikovvv.music_player.core.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import by.bashlikovvv.music_player.ui.theme.DarkColors
import by.bashlikovvv.music_player.ui.theme.LightColors
import by.bashlikovvv.music_player.ui.theme.Typography

@Composable
actual fun PlayerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}