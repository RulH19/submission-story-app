package com.example.appstory.ui.addstory

import android.Manifest

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import com.example.appstory.data.Result
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.appstory.R
import com.example.appstory.databinding.ActivityAddStoryBinding
import com.example.appstory.reduceFileImage
import com.example.appstory.rotateFile
import com.example.appstory.ui.ViewModelFactory
import com.example.appstory.ui.camera.CameraActivity
import com.example.appstory.ui.home.HomeActivity
import com.example.appstory.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
        binding.buttonCamera.setOnClickListener { startCameraX() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.buttonAdd.setOnClickListener { uploadImage() }
    }

    private fun uploadImage() {
        binding.buttonAdd.setOnClickListener {
            val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
            val viewModel: AddStoryViewModel by viewModels { factory }
            if (getFile != null) {
                val file = reduceFileImage(getFile as File)
                val deskripsi = binding.deskripsiGambar.text.toString()
                val description = deskripsi.toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
                val latitude = "-6.273738".toRequestBody("text/plain".toMediaType())
                val longitude = "106.847182".toRequestBody("text/plain".toMediaType())
                val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                viewModel.uploadNewStory(imageMultiPart, description, longitude, latitude).observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Error -> {
                            binding.progressBar.visibility = View.INVISIBLE
                            val error = result.error
                            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                        }
                        is Result.Success -> {
                            binding.progressBar.visibility = View.INVISIBLE
                            Toast.makeText(this, R.string.berhasil_upload, Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(this@AddStoryActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            } else {
                Toast.makeText(this@AddStoryActivity, R.string.silahkan, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddStoryActivity)
                getFile = myFile
                binding.previewImage.setImageURI(uri)
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCameraX =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == CAMERA_X_RESULT) {
                val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.data?.getSerializableExtra("picture", File::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    it.data?.getSerializableExtra("picture")
                } as? File
                val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
                myFile?.let { fileGambar ->
                    rotateFile(fileGambar, isBackCamera)
                    getFile = fileGambar
                    binding.previewImage.setImageBitmap(BitmapFactory.decodeFile(fileGambar.path))
                }
            }

        }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
        launcherIntentCameraX.launch(intent)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, R.string.tidak_dapat_ijin, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}