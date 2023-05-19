package com.example.appstory.ui.home

import android.content.Context
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*

import org.junit.Before
import org.junit.runner.RunWith
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.appstory.data.repository.StoryAppRepository
import com.example.appstory.ui.adapter.StoryAdapter
import com.example.appstory.util.DataDummy
import com.example.appstory.util.MainDispatcherRule
import com.example.appstory.util.getOrAwaitValue
import com.example.storyapp.model.response.ListStoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()
    @Before
    fun setup() {
        context = mock(Context::class.java)
    }
    @Mock
    private lateinit var storyAppRepository: StoryAppRepository
    private lateinit var context :Context


    @Test
    fun `When Get Story Should Not Null and Return Data`() = runTest {

        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data : PagingData<ListStoryItem> = PagingData.from(dummyStory)
        val expectedData = MutableLiveData<PagingData<ListStoryItem>>()
        expectedData.value = data
        Mockito.`when`(storyAppRepository.getStory()).thenReturn(expectedData)

        val homeViewModel = HomeViewModel(storyAppRepository, context)
        val actualStory : PagingData<ListStoryItem> = homeViewModel.story.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0], differ.snapshot()[0])
    }
    @Test
    fun `when Get Story Empty Should Return No Data`()= runTest {
        val data: PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data
        Mockito.`when`(storyAppRepository.getStory()).thenReturn(expectedStory)

        val homeViewModel = HomeViewModel(storyAppRepository, context)
        val actualStory : PagingData<ListStoryItem> = homeViewModel.story.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        assertEquals(0, differ.snapshot().size)
    }
    class QuotePagingSource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {
        companion object {
            fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
                return PagingData.from(items)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
            return LoadResult.Page(emptyList(), 0, 1)
        }
    }
    val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}