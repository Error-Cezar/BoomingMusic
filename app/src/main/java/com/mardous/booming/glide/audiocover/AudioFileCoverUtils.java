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

package com.mardous.booming.glide.audiocover;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class AudioFileCoverUtils {

    private static final String[] FALLBACKS = {"cover.jpg", "album.jpg", "folder.jpg", "cover.png", "album.png", "folder.png"};

    @Nullable
    public static InputStream fallback(@NonNull String path, boolean useFolderImage) throws IOException {
        if (useFolderImage) {
            final File parent = new File(path).getParentFile();
            for (String fallback : FALLBACKS) {
                File cover = new File(parent, fallback);
                if (cover.exists()) {
                    return Files.newInputStream(cover.toPath());
                }
            }
        }
        return null;
    }
}
