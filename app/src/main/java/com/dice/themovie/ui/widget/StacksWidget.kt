package com.dice.themovie.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.dice.themovie.R
import com.dice.themovie.service.StackWidgetService
import com.dice.themovie.ui.activity.DetailMovieActivity

/**
 * Implementation of App Widget functionality.
 */
class StacksWidget : AppWidgetProvider() {

    companion object {

        private const val CLICK_ITEM_ACTION = "com.dice.themovie.CLICK_ITEM_ACTION"
        const val EXTRA_ITEM = "com.dice.themovie.EXTRA_ITEM"
        const val UPDATE_WIDGET = "com.dice.themovie.UPDATE_WIDGET"

        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            // Construct the RemoteViews object
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(context.packageName, R.layout.stacks_widget)
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.empty_view)

            // When clicked an item in stack view
            val clickIntent = Intent(context, StacksWidget::class.java)
            clickIntent.action = CLICK_ITEM_ACTION
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val clickPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setPendingIntentTemplate(R.id.stack_view, clickPendingIntent)


            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action != null) {
            if (intent.action == CLICK_ITEM_ACTION) {
                val movieId = intent.getStringExtra(EXTRA_ITEM)

                val detailIntent = Intent(context, DetailMovieActivity::class.java)
                detailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                detailIntent.putExtra(DetailMovieActivity.EXTRA_ID_STRING, movieId)

                context.startActivity(detailIntent)

                Log.d("stackWidget", "clicked id : $movieId")
            }

            if (intent.action == UPDATE_WIDGET) {
                val widgetManager = AppWidgetManager.getInstance(context)
                val widgetIds =
                    widgetManager.getAppWidgetIds(ComponentName(context, StacksWidget::class.java))

                for (widgetId in widgetIds) {
                    widgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.stack_view)
                }

                Log.d("stackWidget", "updating")
            }
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

