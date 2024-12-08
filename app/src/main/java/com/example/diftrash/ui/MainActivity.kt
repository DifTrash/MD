package com.example.diftrash.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.diftrash.R
import com.example.diftrash.databinding.ActivityMainBinding
import com.example.diftrash.ui.history.HistoryFragment
import com.example.diftrash.ui.home.HomeFragment
import com.example.diftrash.ui.profile.ProfileFragment
import com.example.diftrash.ui.setting.SettingActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment())
                R.id.history -> replaceFragment(HistoryFragment())
                R.id.profile -> replaceFragment(ProfileFragment())

                else -> {
                }
            }

            true
        }
        setOptionMenuTop()
    }

    private fun setOptionMenuTop() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.setting -> {
                    startActivity(Intent(this, SettingActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        Log.d("MainActivity", "Replacing fragment with ${fragment.javaClass.simpleName}")
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.linear_layout, fragment)
        fragmentTransaction.commit()
    }
}