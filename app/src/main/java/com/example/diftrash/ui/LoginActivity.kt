package com.example.diftrash.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.diftrash.R
import com.example.diftrash.ui.login.InputLoginFragment

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        // Pemeriksaan login
        val sharedPreferences = getSharedPreferences("namauser", Context.MODE_PRIVATE)
        val loggedInUser = sharedPreferences.getString("logged_in_user", null)
        if (loggedInUser != null) {
            // Pengguna sudah login, arahkan ke halaman utama
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else {
            // Pengguna belum login, tampilkan halaman login
            supportFragmentManager.beginTransaction().add(R.id.frm_container_login, InputLoginFragment()).commit()
        }
    }
}