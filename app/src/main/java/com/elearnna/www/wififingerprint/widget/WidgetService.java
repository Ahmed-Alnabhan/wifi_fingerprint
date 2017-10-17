package com.elearnna.www.wififingerprint.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Ahmed on 10/16/2017.
 */

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        RemoteViewsFactory rvf = new WidgetFactory(this.getApplication().getApplicationContext(), intent);
        return (rvf);
    }
}
