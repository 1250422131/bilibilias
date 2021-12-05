package com.imcys.bilibilias.Widget.Adapter;

import android.content.Context;

public class Bangumi {
    private String title;
    private int seasonId;
    private String badge;
    private String indexProgress;
    private String progress;
    private String cover;
    private Context context;

    public Bangumi(String title, int seasonId, String badge, String indexProgress, String progress, String cover, Context context) {
        this.title = title;
        this.seasonId = seasonId;
        this.badge = badge;
        this.indexProgress = indexProgress;
        this.progress = progress;
        this.cover = cover;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public String getTitle() {
        return title;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public String getBadge() {
        return badge;
    }

    public String getCover() {
        return cover;
    }

    public String getIndexProgress() {
        return indexProgress;
    }

    public String getProgress() {
        return progress;
    }
}

