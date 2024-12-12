package com.example.diftrash.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.diftrash.R
import com.example.diftrash.databinding.ActivityLoginBinding
import com.example.diftrash.ui.login.InputLoginFragment
import com.example.diftrash.ui.signup.SignupFragment
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            refreshToken()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frm_container_login, InputLoginFragment())
                .commit()
        }
    }

    private fun refreshToken() {
        firebaseAuth.currentUser?.getIdToken(true)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken = task.result?.token
                } else {
                    Toast.makeText(this, "Failed to refresh token", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun navigateToLoginFragment() {
        supportFragmentManager.commit {
            replace(R.id.frm_container_login, InputLoginFragment())
            addToBackStack(null)
        }
    }

    fun navigateToSignupFragment() {
        supportFragmentManager.commit {
            replace(R.id.frm_container_login, SignupFragment())
            addToBackStack(null)
        }
    }
}
