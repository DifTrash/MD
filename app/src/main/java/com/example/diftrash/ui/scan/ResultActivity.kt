package com.example.diftrash.ui.scan

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.diftrash.R
import com.example.diftrash.databinding.ActivityResultBinding
import com.example.diftrash.ui.MainActivity

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    private lateinit var confidenceTextView: TextView
    private lateinit var typeTextView: TextView
    private lateinit var photoView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        confidenceTextView = findViewById(R.id.detail_result)
        typeTextView = findViewById(R.id.recommendation)

        val imageUriString = intent.getStringExtra("imageUri")
        val confidence = intent.getDoubleExtra("confidence", 0.0)
        val type = intent.getStringExtra("type") ?: "No type"
        photoView = findViewById(R.id.photoView)


        showResult(confidence, type)
        showImage(imageUriString)
        initializeUI()

    }

    private fun showResult(confidence: Double, type: String) {
        val formattedConfidence = String.format("%.2f%%", confidence * 100)
        confidenceTextView.text = formattedConfidence
        typeTextView.text = "$type"
    }


    private fun showImage(imageUriString: String?) {
        imageUriString?.let {
            val imageUri = Uri.parse(it)
            photoView.setImageURI(imageUri)
        }
    }

    private fun initializeUI() {
        val backButton: LinearLayout = findViewById(R.id.back)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}


