package com.dice.themovie.service

import android.content.Intent
import android.widget.RemoteViewsService
import com.dice.themovie.adapter.StackRemoteViewsFactory

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}