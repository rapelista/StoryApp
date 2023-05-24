@file:Suppress("DEPRECATION")

package com.gvstang.dicoding.latihan.storyapp.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.gvstang.dicoding.latihan.storyapp.adapter.StoryPagingAdapter
import com.gvstang.dicoding.latihan.storyapp.api.response.Story
import com.gvstang.dicoding.latihan.storyapp.data.StoryRepository
import com.gvstang.dicoding.latihan.storyapp.util.DataDummy
import com.gvstang.dicoding.latihan.storyapp.util.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mainViewModel: MainViewModel
    private val dummyStory = DataDummy.generate()
    private val dispatcher = TestCoroutineDispatcher()
    private val differ = AsyncPagingDataDiffer(
        diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
        updateCallback = TestListCallback(),
        workerDispatcher = Dispatchers.Main
    )
    private val TOKEN = "TOKEN"

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mainViewModel = MainViewModel(storyRepository = storyRepository)
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `ketika berhasil memuat data berita`() = runTest {
        val expectedResultStories = MutableLiveData<PagingData<Story>>()
        val expectedDataPaging = PagingData.from(dummyStory)
        expectedResultStories.value = expectedDataPaging

        `when`(storyRepository.getStories(TOKEN)).thenReturn(expectedResultStories)
        val actualResultStories = mainViewModel.stories(TOKEN).getOrAwaitValue()
        differ.submitData(actualResultStories)

        val dataActual = differ.snapshot().items

        Mockito.verify(storyRepository).getStories(TOKEN)
        assertNotNull(dataActual)
        assertEquals(dataActual.size, dummyStory.size)
        assertEquals(dataActual[0].name, dummyStory[0].name)
    }

    @Test
    fun `ketika gagal memuat data berita`() = runTest {
        val emptyResultStories = MutableLiveData<PagingData<Story>>()
        val emptyDataPaging = PagingData.empty<Story>()
        emptyResultStories.value = emptyDataPaging

        `when`(storyRepository.getStories(TOKEN)).thenReturn(emptyResultStories)
        val actualResultStories = mainViewModel.stories(TOKEN).getOrAwaitValue()
        differ.submitData(actualResultStories)

        val dataActual = differ.itemCount

        Mockito.verify(storyRepository).getStories(TOKEN)
        assertEquals(0, dataActual)
    }

    class TestListCallback : ListUpdateCallback {
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
    }
}