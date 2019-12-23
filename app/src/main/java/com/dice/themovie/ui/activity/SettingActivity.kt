package com.dice.themovie.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.dice.themovie.R
import com.dice.themovie.receiver.AlarmReceiver
import kotlinx.android.synthetic.main.activity_setting.*
import java.util.*

class SettingActivity : AppCompatActivity() {

    private val alarmReceiver = AlarmReceiver()
    private val calendar = Calendar.getInstance()
    private lateinit var settingPreference: SharedPreferences

    companion object {
        const val SETTING_PREF = "setting_pref"
        const val DAILY_REMINDER = "daily_reminder"
        const val RELEASE_REMINDER = "release_reminder"
        const val APP_LANGUAGE = "app_language"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        settingPreference = getSharedPreferences(SETTING_PREF, Context.MODE_PRIVATE)

        val appLanguage = settingPreference.getString(APP_LANGUAGE, Locale.getDefault().displayLanguage)

        if (appLanguage == "en" || appLanguage == "english" || appLanguage == "English") {
            btnLanguage.setImageResource(R.drawable.english_flag)
        } else {
            btnLanguage.setImageResource(R.drawable.indonesian_flag)
        }

        setSwitchListener()

        btnLanguage.setOnClickListener{
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    @Suppress("DEPRECATION")
    private fun setLanguage(langKey: String?) {
        if (langKey != null){
            val locale = Locale(langKey)
            Locale.setDefault(locale)

            val config = Configuration()
            config.locale = locale

            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

            Log.d("language", "changed to $langKey")
        }
    }

    private fun setSwitchListener() {
        switchDailyReminder.isChecked = settingPreference.getBoolean(DAILY_REMINDER, false)
        switchReleaseReminder.isChecked = settingPreference.getBoolean(RELEASE_REMINDER, false)

        switchDailyReminder.setOnCheckedChangeListener { _, isChecked ->
            val editor = settingPreference.edit()

            if (isChecked) {
                // at 7 AM
                calendar.set(Calendar.HOUR_OF_DAY, 7)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)

                alarmReceiver.setAlarm(this, calendar.timeInMillis, AlarmReceiver.ALARM_ID_REMINDER)
                editor.putBoolean(DAILY_REMINDER, true)
            } else {
                alarmReceiver.cancelAlarm(this, AlarmReceiver.ALARM_ID_REMINDER)
                editor.putBoolean(DAILY_REMINDER, false)
            }

            editor.apply()
        }

        switchReleaseReminder.setOnCheckedChangeListener { _, isChecked ->
            val editor = settingPreference.edit()

            if (isChecked) {
                // at 8 AM
                calendar.set(Calendar.HOUR_OF_DAY, 8)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)

                alarmReceiver.setAlarm(this, calendar.timeInMillis, AlarmReceiver.ALARM_ID_RELEASE)
                editor.putBoolean(RELEASE_REMINDER, true)
            } else {
                alarmReceiver.cancelAlarm(this, AlarmReceiver.ALARM_ID_RELEASE)
                editor.putBoolean(RELEASE_REMINDER, false)
            }

            editor.apply()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
