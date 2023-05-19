package com.example.appstory.util

import com.example.storyapp.model.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val storyItem = ListStoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1683978720584_TEYEAfx8.jpg",
                "2023-05-13T11:52:00.586Z",
                "reviewer13",
                "lwkenlwkern",
                        -6.9175,
                "story-O0IyxFNp-PrwxNcE",
                107.6191,
            )
            items.add(storyItem)
        }
        return items
    }
}