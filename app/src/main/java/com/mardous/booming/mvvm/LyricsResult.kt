/*
 * Copyright (c) 2024 Christians Martínez Alvarado
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mardous.booming.mvvm

import android.content.Context
import android.net.Uri
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.mardous.booming.lyrics.Lyrics
import com.mardous.booming.R
import java.io.File
import java.util.EnumMap

class SaveLyricsResult(
    val isPending: Boolean,
    val isSuccess: Boolean,
    val pendingWrite: List<Pair<File, Uri>>? = null
)

@Immutable
class LyricsResult(
    val id: Long,
    val plainLyrics: String? = null,
    val syncedLyrics: Lyrics? = null,
    val sources: Map<LyricsType, LyricsSource> = EnumMap(LyricsType::class.java),
    val loading: Boolean = false,
) {
    val hasPlainLyrics: Boolean get() = !plainLyrics.isNullOrEmpty()
    val hasSyncedLyrics: Boolean get() = syncedLyrics?.hasContent == true
    val isEmpty: Boolean get() = !hasPlainLyrics && !hasSyncedLyrics

    companion object {
        val Empty = LyricsResult(-1)
    }
}

enum class LyricsType(@IdRes val idRes: Int) {
    Embedded(R.id.embeddedButton),
    External(R.id.externalButton);

    val isExternal get() = this == External
}

enum class LyricsSource(
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int = 0,
    private val helpShownKey: String = ""
) {
    Downloaded(R.string.downloaded_lyrics),
    Embedded(R.string.embedded_lyrics),
    EmbeddedSynced(
        R.string.embedded_lyrics,
        R.string.lyrics_source_embedded_synced,
        helpShownKey = "lyrics_help_embedded_synced"
    ),
    Lrc(
        R.string.lrc_lyrics,
        R.string.lyrics_source_lrc_file,
        helpShownKey = "lyrics_help_lrc"
    );

    fun canShowHelp(context: Context): Boolean =
        descriptionRes != 0 && helpShownKey.isNotEmpty() &&
                !PreferenceManager.getDefaultSharedPreferences(context).getBoolean(helpShownKey, false)

    fun setHelpShown(context: Context) {
        if (helpShownKey.isNotEmpty()) {
            PreferenceManager.getDefaultSharedPreferences(context).edit {
                putBoolean(helpShownKey, true)
            }
        }
    }
}