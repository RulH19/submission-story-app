package com.example.appstory.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.appstory.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private val binding: ActivityDetailStoryBinding by lazy {
        ActivityDetailStoryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        val name = intent.getStringExtra(NAME_EXTRA)
        val description = intent.getStringExtra(DESCRIPTION_EXTRA)
        val imgUrl = intent.getStringExtra(IMAGE_URL_EXTRA)

        supportActionBar?.title = name
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = name
        binding.description.text = description
        Glide.with(this)
            .load(imgUrl)
            .into(binding.photo)
    }

    companion object {
        const val NAME_EXTRA = "name_extra"
        const val DESCRIPTION_EXTRA = "desc_extra"
        const val IMAGE_URL_EXTRA = "img_extra"
    }
}