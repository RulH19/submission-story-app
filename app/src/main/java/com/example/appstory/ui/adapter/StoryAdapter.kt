package com.example.appstory.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.appstory.databinding.StoryItemBinding
import com.example.appstory.ui.detail.DetailStoryActivity
import com.example.storyapp.model.response.ListStoryItem

class StoryAdapter :
    PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {
    class StoryViewHolder(val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.tvUserName.text = story.name
            binding.tvCaption.text = story.description
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(binding.imgPost)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
        holder.binding.root.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailStoryActivity::class.java)
            intent.putExtra(DetailStoryActivity.NAME_EXTRA, story?.name)
            intent.putExtra(DetailStoryActivity.DESCRIPTION_EXTRA, story?.description)
            intent.putExtra(DetailStoryActivity.IMAGE_URL_EXTRA, story?.photoUrl)
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.binding.imgPost, "picture"),
                    Pair(holder.binding.tvCaption, "description")
                )
            holder.itemView.context.startActivity(intent, optionsCompat.toBundle())
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem,
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}

