package com.example.diftrash.ui.setting

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import com.example.diftrash.R

class SettingActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var themeSwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)

        themeSwitch = findViewById(R.id.swith_theme)

        // Load saved theme preference
        val isDarkMode = sharedPreferences.getBoolean("DARK_MODE", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            themeSwitch.isChecked = true
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // Switch listener to change theme
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                saveThemePreference(true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                saveThemePreference(false)
            }
        }
    }

    private fun saveThemePreference(isDarkMode: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("DARK_MODE", isDarkMode)
        editor.apply()
    }
}
