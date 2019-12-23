package com.dice.themovie.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dice.themovie.R
import com.dice.themovie.model.MovieList
import com.dice.themovie.model.network.ApiClient
import com.dice.themovie.ui.activity.DetailMovieActivity
import com.dice.themovie.ui.activity.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val NOTIFY_REMINDER = "daily_reminder"
        private const val NOTIFY_RELEASE = "daily_release"
        private const val TAG = "AlarmReceiver"

        const val ALARM_ID_REMINDER = 100
        const val ALARM_ID_RELEASE = 200
        var REQUEST_CODE = 900
    }

    private val apiService = ApiClient.createService()
    private val apiKey = ApiClient.API_KEY

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            NOTIFY_REMINDER -> {
                val title = context.getString(R.string.daily_reminder)
                val message = context.getString(R.string.come_back)

                showNotification(context, title, message, ALARM_ID_REMINDER, null)
            }

            NOTIFY_RELEASE -> {
                val title = context.getString(R.string.new_release)
                val message = context.getString(R.string.has_released)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val currentDate = dateFormat.format(Date())

                apiService.getReleaseMovie(apiKey, currentDate, currentDate).enqueue(object :
                    Callback<MovieList> {
                    override fun onFailure(call: Call<MovieList>, t: Throwable) {
                        Toast.makeText(context, "Failed connect to server", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                        if (response.isSuccessful) {
                            val movieList = response.body()?.results

                            var alarmId = ALARM_ID_RELEASE
                            if (movieList != null) {
                                for (movie in movieList) {
                                    showNotification(
                                        context,
                                        title,
                                        movie.title + message,
                                        alarmId++,
                                        movie.id
                                    )

                                    REQUEST_CODE = alarmId
                                }
                            }
                        }
                    }

                })
            }
        }
    }

    private fun showNotification(context: Context, title: String, message: String, notifyId: Int, idMovie: String?) {
        val channelId = "Channel_1"
        val channelName = "AlarmManager Channel"

        val intent = if (idMovie != null){
            Intent(context, DetailMovieActivity::class.java).putExtra(DetailMovieActivity.EXTRA_ID_STRING, idMovie)
        }else{
            Intent(context, MainActivity::class.java)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManager.notify(notifyId, notification)
    }

    fun setAlarm(context: Context, timeInMillis: Long, alarmId: Int) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.action = if (alarmId == ALARM_ID_REMINDER) {
            NOTIFY_REMINDER
        } else {
            NOTIFY_RELEASE
        }

        val pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0)

        //repeating alarm
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Log.d(TAG, "id : $alarmId ,alarm created")
    }

    fun cancelAlarm(context: Context, alarmId: Int) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0)

        alarmManager.cancel(pendingIntent)

        Log.d(TAG, "id : $alarmId ,alarm cancelled")
    }

}
