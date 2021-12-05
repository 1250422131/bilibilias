package com.imcys.bilibilias.Widget.Adapter;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class BangumiRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BangumiRemoteViewsFactory(this.getApplicationContext(), intent);
    }

}
