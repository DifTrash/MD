package com.example.diftrash.ui.scan

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.diftrash.R
import com.example.diftrash.databinding.ActivityScanBinding
import com.example.diftrash.retrofit.ApiConfig
import com.example.diftrash.retrofit.getImageUri
import com.example.diftrash.retrofit.uriToFile
import com.example.diftrash.ui.MainActivity
import com.example.diftrash.ui.scan.ResultActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private val mGalleryRequestCode = 2
    private val mCameraRequestCode = 0
    private val CAMERA_PERMISSION_CODE = 101
    private val STORAGE_PERMISSION_CODE = 102

    private lateinit var photoImageView: ImageView
    private var currentImageUri: Uri? = null
    private var getFile: File? = null
    private lateinit var currentPhotoPath: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        binding.btnResult.visibility = INVISIBLE


        photoImageView = binding.photoView

        binding.linearLayoutScanCamera.setOnClickListener { startCamera() }
        binding.linearLayoutScanGallery.setOnClickListener { startGallery() }
        binding.btnResult.setOnClickListener { uploadImage() }

        initializeUI()
    }

    private fun startCamera(){
        binding.linearLayoutScanCamera.setOnClickListener {
            requestCameraPermission()
        }
    }

    private fun startGallery() {
        binding.linearLayoutScanGallery.setOnClickListener {
            requestStoragePermission()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.photoView.setImageURI(it)
            binding.btnResult.visibility = VISIBLE

        }
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }
    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            && shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
        } else {
            openGallery()
        }
    }

    private fun openCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }
    private fun openGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            mCameraRequestCode -> {
                if (resultCode == Activity.RESULT_OK) {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    photoImageView.setImageBitmap(imageBitmap)
                }
            }
            mGalleryRequestCode -> {
                if (resultCode == Activity.RESULT_OK) {
                    val selectedImageUri: Uri? = data?.data
                    photoImageView.setImageURI(selectedImageUri)

                    val imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImageUri)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    val errorMessage = getString(R.string.camera_permission_required)
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
            STORAGE_PERMISSION_CODE -> {
                if (requestCode == STORAGE_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    val errorMessage = getString(R.string.storage_permission_required)
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this)
            Log.d("Image File", "showImage: ${imageFile.path}")

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "file", // Nama field sesuai API
                imageFile.name,
                requestImageFile
            )

            showLoading(true)

            lifecycleScope.launch {
                try {
                    val apiService = ApiConfig.getApiService()
                    val uid = getUidFromFirebase()
                    val response = apiService.uploadImage(uid, multipartBody)

                    val confidence = response.data?.data?.confidence as? Double ?: 0.0
                    val type = response.data?.data?.type ?: "Unknown"
                    val message = response.data?.message ?: "No message"

                    Log.d("Prediction Response", "Type: $type, Confidence: $confidence, Message: $message")

                    showLoading(false)

                    val intent = Intent(this@ScanActivity, ResultActivity::class.java)
                    intent.putExtra("imageUri", currentImageUri.toString())
                    intent.putExtra("type", type)
                    intent.putExtra("confidence", confidence)
                    intent.putExtra("message", message)
                    startActivity(intent)

                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorMessage = if (!errorBody.isNullOrEmpty()) {
                        JSONObject(errorBody).getString("message")
                    } else {
                        getString(R.string.upload_failed)
                    }
                    Log.e("Upload Error", errorMessage)
                    showToast(errorMessage)
                    showLoading(false)
                } catch (e: Exception) {
                    Log.e("Unexpected Error", e.localizedMessage ?: "Unknown error")
                    showToast(getString(R.string.unexpected_error))
                    showLoading(false)
                }
            }
        } ?: run {
            showToast(getString(R.string.no_image_selected))
        }
    }

    private fun getUidFromFirebase(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid ?: "UnknownUID"
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
