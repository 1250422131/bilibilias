package com.imcys.bilibilias.Widget.Adapter;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class MyRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyRemoteViewsFactory(this.getApplicationContext(), intent);
    }


}