package com.example.appstory.ui.home


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appstory.R
import com.example.appstory.data.pref.UserPreference
import com.example.appstory.databinding.ActivityHomeBinding
import com.example.appstory.ui.map.MapsActivity
import com.example.appstory.ui.adapter.LoadingStateAdapter
import com.example.appstory.ui.adapter.StoryAdapter
import com.example.appstory.ui.addstory.AddStoryActivity
import com.example.appstory.ui.main.MainActivity

class HomeActivity : AppCompatActivity() {
    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupAction()

        binding.tambahStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.logout -> {
                val alert = AlertDialog.Builder(this)
                alert.setTitle(R.string.peringatan).setPositiveButton(R.string.yakin) { _, _ ->
                    UserPreference(this).userLogout()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }.setNegativeButton(R.string.batal, null)
                val alertCreate = alert.create()
                alertCreate.show()
                true
            }
            R.id.maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupAction() {
        val adapter = StoryAdapter()
        binding.rvUsers.adapter = adapter
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        viewModel.story.observe(this){
            adapter.submitData(lifecycle, it)
        }
        binding.rvUsers.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, getString(R.string.dibutuhkan_izin), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}