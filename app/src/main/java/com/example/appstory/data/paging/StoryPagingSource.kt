package com.example.appstory.data.paging

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.appstory.data.model.ApiService
import com.example.appstory.data.pref.UserPreference
import com.example.storyapp.model.response.ListStoryItem

class StoryPagingSource(
    private val apiService: ApiService,
    private val context: Context,
) : PagingSource<Int, ListStoryItem>() {

    private val PAGE_INDEX = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: PAGE_INDEX
            val param = params.loadSize
            val token = "Bearer ${UserPreference(context).getUser().token}"
            val response = apiService.getStories(token, position, param).listStory.toList()

            LoadResult.Page(
                data = response,
                prevKey = if (position == PAGE_INDEX) null else position - 1,
                nextKey = position.plus(1)
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
