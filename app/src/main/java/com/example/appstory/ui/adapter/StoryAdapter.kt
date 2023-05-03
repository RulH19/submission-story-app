package com.example.appstory.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListAdapter
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appstory.data.room.StoryEntity
import com.example.appstory.databinding.StoryItemBinding
import com.example.storyapp.model.response.ListStoryItem
import com.example.storyapp.model.response.StoryResponse

class StoryAdapter (private val context: Context, private val list: List<StoryEntity>) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {
    inner class StoryViewHolder(val binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryAdapter.StoryViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun getItemCount()= list.size

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.binding.tvUserName.text = list[position].name
        holder.binding.tvCaption.text = list[position].description
        Glide.with(holder.itemView.context)
            .load(list[position].photoUrl)
            .into(holder.binding.imgPost)
    }

}