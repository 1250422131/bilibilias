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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import androidx.annotation.NonNull;
import android.util.SparseArray;

import com.liulishuo.okdownload.core.exception.SQLiteException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.liulishuo.okdownload.core.breakpoint.BreakpointSQLiteKey.BLOCK_INDEX;
import static com.liulishuo.okdownload.core.breakpoint.BreakpointSQLiteKey.CHUNKED;
import static com.liulishuo.okdownload.core.breakpoint.BreakpointSQLiteKey.CONTENT_LENGTH;
import static com.liulishuo.okdownload.core.breakpoint.BreakpointSQLiteKey.CURRENT_OFFSET;
import static com.liulishuo.okdownload.core.breakpoint.BreakpointSQLiteKey.ETAG;
import static com.liulishuo.okdownload.core.breakpoint.BreakpointSQLiteKey.FILENAME;
import static com.liulishuo.okdownload.core.breakpoint.BreakpointSQLiteKey.HOST_ID;
import static com.liulishuo.okdownload.core.breakpoint.BreakpointSQLiteKey.ID;
import static com.liulishuo.okdownload.core.breakpoint.BreakpointSQLiteKey.PARENT_PATH;
import static com.liulishuo.okdownload.core.breakpoint.BreakpointSQLiteKey.START_OFFSET;
import static com.liulishuo.okdownload.core.breakpoint.BreakpointSQLiteKey.TASK_ONLY_PARENT_PATH;
import static com.liulishuo.okdownload.core.breakpoint.BreakpointSQLiteKey.URL;

public class BreakpointSQLiteHelper extends SQLiteOpenHelper {

    private static final String NAME = "okdownload-breakpoint.db";
    private static final int VERSION = 3;

    private static final String RESPONSE_FILENAME_TABLE_NAME = "okdownloadResponseFilename";
    private static final String BREAKPOINT_TABLE_NAME = "breakpoint";
    private static final String BLOCK_TABLE_NAME = "block";
    static final String TASK_FILE_DIRTY_TABLE_NAME = "taskFileDirty";

    public BreakpointSQLiteHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setWriteAheadLoggingEnabled(true);
        } else {
            db.enableWriteAheadLogging();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateTable.BREAKPOINT);

        db.execSQL(CreateTable.BLOCK);

        db.execSQL(CreateTable.RESPONSE_FILENAME);

        db.execSQL(CreateTable.TASK_FILE_DIRTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            db.execSQL(Migration.RESPONSE_FILENAME_1_TO_2);
        }

        if (oldVersion <= 2) {
            db.execSQL(Migration.TASK_FILE_DIRTY_NONE_TO_2);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void markFileDirty(int id) {
        final SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues(1);
        values.put(ID, id);
        db.insert(TASK_FILE_DIRTY_TABLE_NAME, null, values);
    }

    public void markFileClear(int id) {
        getWritableDatabase().delete(TASK_FILE_DIRTY_TABLE_NAME, ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public List<Integer> loadDirtyFileList() {
        final List<Integer> dirtyFileList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = getWritableDatabase().rawQuery(Select.ALL_FROM_TASK_FILE_DIRTY,
                    null);
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(ID);
                dirtyFileList.add(cursor.getInt(idIndex));
            }
        } finally {
            if (cursor != null) cursor.close();
        }

        return dirtyFileList;
    }

    public SparseArray<BreakpointInfo> loadToCache() {
        Cursor breakpointCursor = null;
        Cursor blockCursor = null;
        final SQLiteDatabase db = getWritableDatabase();

        final List<BreakpointInfoRow> breakpointInfoRows = new ArrayList<>();
        final List<BlockInfoRow> blockInfoRows = new ArrayList<>();

        try {
            breakpointCursor = db.rawQuery(Select.ALL_FROM_BREAKPOINT, null);
            while (breakpointCursor.moveToNext()) {
                breakpointInfoRows.add(new BreakpointInfoRow(breakpointCursor));
            }
            blockCursor = db.rawQuery(Select.ALL_FROM_BLOCK, null);
            while (blockCursor.moveToNext()) {
                blockInfoRows.add(new BlockInfoRow(blockCursor));
            }
        } finally {
            if (breakpointCursor != null) breakpointCursor.close();
            if (blockCursor != null) blockCursor.close();
        }

        final SparseArray<BreakpointInfo> breakpointInfoMap = new SparseArray<>();

        for (BreakpointInfoRow infoRow : breakpointInfoRows) {
            final BreakpointInfo info = infoRow.toInfo();
            final Iterator<BlockInfoRow> blockIt = blockInfoRows.iterator();
            while (blockIt.hasNext()) {
                final BlockInfoRow blockInfoRow = blockIt.next();
                if (blockInfoRow.getBreakpointId() == info.id) {
                    info.addBlock(blockInfoRow.toInfo());
                    blockIt.remove();
                }
            }
            breakpointInfoMap.put(info.id, info);
        }

        return breakpointInfoMap;
    }

    public HashMap<String, String> loadResponseFilenameToMap() {
        Cursor cursor = null;
        final SQLiteDatabase db = getWritableDatabase();
        final HashMap<String, String> urlFilenameMap = new HashMap<>();

        try {
            cursor = db.rawQuery(Select.ALL_FROM_RESPONSE_FILENAME, null);
            while (cursor.moveToNext()) {
                int urlIndex = cursor.getColumnIndex(URL);
                final String url = urlIndex >=0 ? cursor.getString(urlIndex) : "";

                int fileNameIndex = cursor.getColumnIndex(FILENAME);
                final String filename = fileNameIndex >= 0 ? cursor.getString(fileNameIndex) : "";
                urlFilenameMap.put(url, filename);
            }
        } finally {
            if (cursor != null) cursor.close();
        }

        return urlFilenameMap;
    }

    public void updateFilename(@NonNull String url, @NonNull String filename) {
        final SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues(2);
        values.put(URL, url);
        values.put(FILENAME, filename);

        Cursor c = null;
        synchronized (url.intern()) {
            try {
                c = db.rawQuery(Select.FILENAME_FROM_RESPONSE_FILENAME_BY_URL, new String[]{url});
                if (c.moveToFirst()) {
                    // exist
                    int fileNameIndex = c.getColumnIndex(FILENAME);
                    if (!filename.equals(c.getString(fileNameIndex))) {
                        // replace if not equal
                        db.replace(RESPONSE_FILENAME_TABLE_NAME, null, values);
                    }
                } else {
                    // insert
                    db.insert(RESPONSE_FILENAME_TABLE_NAME, null, values);
                }
            } finally {
                if (c != null) c.close();
            }
        }
    }

    public void insert(@NonNull BreakpointInfo info) throws IOException {
        final int blockCount = info.getBlockCount();
        final SQLiteDatabase db = getWritableDatabase();
        for (int i = 0; i < blockCount; i++) {
            final BlockInfo blockInfo = info.getBlock(i);
            if (db.insert(BLOCK_TABLE_NAME, null, toValues(info.id, i, blockInfo)) == -1) {
                throw new SQLiteException("insert block " + blockInfo + " failed!");
            }
        }

        final long result = db.insert(BREAKPOINT_TABLE_NAME, null,
                toValues(info));
        if (result == -1) throw new SQLiteException("insert info " + info + " failed!");

    }

    public void updateBlockIncrease(@NonNull BreakpointInfo info, int blockIndex,
                                    long newCurrentOffset) {
        final ContentValues values = new ContentValues();
        values.put(CURRENT_OFFSET, newCurrentOffset);
        getWritableDatabase().update(BLOCK_TABLE_NAME, values,
                HOST_ID + " = ? AND " + BLOCK_INDEX + " = ?",
                new String[]{Integer.toString(info.id), Integer.toString(blockIndex)});
    }

    public void updateInfo(@NonNull BreakpointInfo info) throws IOException {
        final SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        db.beginTransaction();
        try {
            cursor = getWritableDatabase().rawQuery(Select.ID_FROM_BREAKPOINT_BY_ID,
                    new String[]{Integer.toString(info.id)});
            if (!cursor.moveToNext()) return; // not exist

            // update
            removeInfo(info.id);
            insert(info);

            db.setTransactionSuccessful();
        } finally {
            if (cursor != null) cursor.close();
            db.endTransaction();
        }
    }

    public void removeInfo(int id) {
        getWritableDatabase().delete(BREAKPOINT_TABLE_NAME, ID + " = ?",
                new String[]{String.valueOf(id)});
        removeBlock(id);
    }

    public void removeBlock(int breakpointId) {
        getWritableDatabase().delete(BLOCK_TABLE_NAME, HOST_ID + " = ?",
                new String[]{String.valueOf(breakpointId)});
    }

    private static ContentValues toValues(@NonNull BreakpointInfo info) {
        final ContentValues values = new ContentValues();
        values.put(ID, info.id);
        values.put(URL, info.getUrl());
        values.put(ETAG, info.getEtag());
        values.put(PARENT_PATH, info.parentFile.getAbsolutePath());
        values.put(FILENAME, info.getFilename());
        values.put(TASK_ONLY_PARENT_PATH, info.isTaskOnlyProvidedParentPath() ? 1 : 0);
        values.put(CHUNKED, info.isChunked() ? 1 : 0);

        return values;
    }

    private static ContentValues toValues(int breakpointId, int index, @NonNull BlockInfo info) {
        final ContentValues values = new ContentValues();
        values.put(HOST_ID, breakpointId);
        values.put(BLOCK_INDEX, index);
        values.put(START_OFFSET, info.getStartOffset());
        values.put(CONTENT_LENGTH, info.getContentLength());
        values.put(CURRENT_OFFSET, info.getCurrentOffset());
        return values;
    }


    private interface CreateTable {
        static final String BREAKPOINT = "CREATE TABLE IF NOT EXISTS "
                + BREAKPOINT_TABLE_NAME + "( "
                + ID + " INTEGER PRIMARY KEY, "
                + URL + " VARCHAR NOT NULL, "
                + ETAG + " VARCHAR, "
                + PARENT_PATH + " VARCHAR NOT NULL, "
                + FILENAME + " VARCHAR, "
                + TASK_ONLY_PARENT_PATH + " TINYINT(1) DEFAULT 0, "
                + CHUNKED + " TINYINT(1) DEFAULT 0)";

        static final String BLOCK = "CREATE TABLE IF NOT EXISTS "
                + BLOCK_TABLE_NAME + "( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HOST_ID + " INTEGER, "
                + BLOCK_INDEX + " INTEGER, "
                + START_OFFSET + " INTEGER, "
                + CONTENT_LENGTH + " INTEGER, "
                + CURRENT_OFFSET + " INTEGER)";

        static final String RESPONSE_FILENAME = "CREATE TABLE IF NOT EXISTS "
                + RESPONSE_FILENAME_TABLE_NAME + "( "
                + URL + " VARCHAR NOT NULL PRIMARY KEY, "
                + FILENAME + " VARCHAR NOT NULL)";

        static final String TASK_FILE_DIRTY = "CREATE TABLE IF NOT EXISTS "
                + TASK_FILE_DIRTY_TABLE_NAME + "( "
                + ID + " INTEGER PRIMARY KEY)";
    }

    private interface Migration {
        static final String RESPONSE_FILENAME_1_TO_2 = CreateTable.RESPONSE_FILENAME;
        static final String TASK_FILE_DIRTY_NONE_TO_2 = CreateTable.TASK_FILE_DIRTY;
    }

    private interface Select {
        static final String ALL_FROM_TASK_FILE_DIRTY = "SELECT * FROM " + TASK_FILE_DIRTY_TABLE_NAME;
        static final String ALL_FROM_BREAKPOINT = "SELECT * FROM " + BREAKPOINT_TABLE_NAME;
        static final String ALL_FROM_BLOCK = "SELECT * FROM " + BLOCK_TABLE_NAME;
        static final String ALL_FROM_RESPONSE_FILENAME = "SELECT * FROM " + RESPONSE_FILENAME_TABLE_NAME;
        static final String FILENAME_FROM_RESPONSE_FILENAME_BY_URL = "SELECT " + FILENAME + " FROM " + RESPONSE_FILENAME_TABLE_NAME
                + " WHERE " + URL + " = ?";
        static final String ID_FROM_BREAKPOINT_BY_ID = "SELECT " + ID + " FROM " + BREAKPOINT_TABLE_NAME + " WHERE " + ID + " = ? LIMIT 1";
    }

}

