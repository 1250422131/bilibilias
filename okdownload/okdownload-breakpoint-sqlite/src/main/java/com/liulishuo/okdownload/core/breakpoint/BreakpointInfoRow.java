/*
 * Copyright (c) 2017 LingoChamp Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liulishuo.okdownload.core.breakpoint;

import android.database.Cursor;

import java.io.File;

import static com.liulishuo.okdownload.core.breakpoint.BreakpointSQLiteKey.CHUNKED;
import static com.liulishuo.okdownload.core.breakpoint.BreakpointSQLiteKey.ETAG;
import static com.liulishuo.okdownload.core.breakpoint.BreakpointSQLiteKey.FILENAME;
import static com.liulishuo.okdownload.core.breakpoint.BreakpointSQLiteKey.ID;
import static com.liulishuo.okdownload.core.breakpoint.BreakpointSQLiteKey.PARENT_PATH;
import static com.liulishuo.okdownload.core.breakpoint.BreakpointSQLiteKey.TASK_ONLY_PARENT_PATH;
import static com.liulishuo.okdownload.core.breakpoint.BreakpointSQLiteKey.URL;

public class BreakpointInfoRow {
    private final int id;
    private final String url;
    private final String etag;
    private final String parentPath;
    private final String filename;
    private final boolean taskOnlyProvidedParentPath;
    private final boolean chunked;

    public BreakpointInfoRow(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(ID);
        this.id = idIndex >=0 ? cursor.getInt(idIndex) : 0;

        int urlIndex = cursor.getColumnIndex(URL);
        this.url = urlIndex >= 0 ? cursor.getString(urlIndex) : "";

        int etagIndex = cursor.getColumnIndex(ETAG);
        this.etag = etagIndex >= 0 ? cursor.getString(etagIndex) : "";

        int parentPathIndex = cursor.getColumnIndex(PARENT_PATH);
        this.parentPath = parentPathIndex >=0 ? cursor.getString(parentPathIndex) : "";

        int fileNameIndex = cursor.getColumnIndex(FILENAME);
        this.filename = fileNameIndex >= 0 ? cursor.getString(fileNameIndex) : "";

        int taskOnlyProvidedParentPathIndex = cursor.getColumnIndex(TASK_ONLY_PARENT_PATH);
        this.taskOnlyProvidedParentPath = taskOnlyProvidedParentPathIndex >= 0 && cursor.getInt(taskOnlyProvidedParentPathIndex) == 1;

        int chunkedIndex = cursor.getColumnIndex(CHUNKED);
        this.chunked = chunkedIndex >= 0 && cursor.getInt(chunkedIndex) == 1;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getEtag() {
        return etag;
    }

    public String getParentPath() {
        return parentPath;
    }

    public String getFilename() {
        return filename;
    }

    public boolean isTaskOnlyProvidedParentPath() {
        return taskOnlyProvidedParentPath;
    }

    public boolean isChunked() {
        return chunked;
    }

    public BreakpointInfo toInfo() {
        final BreakpointInfo info = new BreakpointInfo(id, url, new File(parentPath), filename,
                taskOnlyProvidedParentPath);
        info.setEtag(etag);
        info.setChunked(chunked);
        return info;
    }
}
