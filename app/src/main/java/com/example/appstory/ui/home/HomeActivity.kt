package com.example.appstory.ui.home

import android.Manifest
import android.app.Activity
import com.example.appstory.data.Result
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appstory.R
import com.example.appstory.data.datastore.UserPreference
import com.example.appstory.data.room.StoryEntity
import com.example.appstory.databinding.ActivityHomeBinding
import com.example.appstory.ui.adapter.StoryAdapter
import com.example.appstory.ui.login.LoginActivity
import com.example.appstory.ui.main.MainActivity
import com.example.storyapp.model.response.ListStoryItem

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

class HomeActivity : AppCompatActivity() {
    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModel.HomeViewModelFactory.getInstance(
            this,
            UserPreference.getInstance(dataStore)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        checkIfSessionValid()
    }

    private fun checkIfSessionValid() {
        homeViewModel.checkAvailableToken().observe(this) {
            if (it == "null") {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                setupView("Bearer $it")
            }
        }
    }

    private fun setupView(token: String) {
        homeViewModel.getAllStories(token).observe(this) { result ->
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
                    val data = result.data
                    if (data.isEmpty()) {
                        Toast.makeText(this, "Data Kosong", Toast.LENGTH_SHORT).show()
                    } else {
                        binding.apply {
                            rvUsers.layoutManager = LinearLayoutManager(this@HomeActivity)
                            rvUsers.setHasFixedSize(true)
                            rvUsers.adapter = StoryAdapter(this@HomeActivity, data)
                        }
                    }
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
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, getString(R.string.dibutuhkan_izin), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}