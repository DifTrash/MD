package com.example.diftrash.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.diftrash.R

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        val sharedPreferences: SharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("DARK_MODE", false)

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }


}