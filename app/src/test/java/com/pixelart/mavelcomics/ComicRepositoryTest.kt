package com.pixelart.mavelcomics

import androidx.paging.*
import com.pixelart.mavelcomics.datasource.NetworkResource
import com.pixelart.mavelcomics.datasource.network.NetworkService
import com.pixelart.mavelcomics.models.ComicResponse
import com.pixelart.mavelcomics.repository.ComicsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

@ExperimentalCoroutinesApi
class ComicRepositoryTest {

    private val networkService: NetworkService = mock()

    private lateinit var repository: ComicsRepository

    @Before
    fun setup() {
        repository = FakeRepository(networkService)
    }

    @Test
    fun testFetchComicsShouldReturnPagedData() {
        runBlocking {
            val comicPageData = repository.fetchComics().toList().first()
            val comics = comicPageData.collectData()
            assert(comics.isNotEmpty())
            assert(comics[1].title == comicTestData.dataContainer.results[1].title)
        }
    }

    @Test
    fun fetchComicByIdShouldReturnSuccessData() {
        runBlocking {
            whenever(networkService.getComic("")).thenReturn(Response.success(comicTestData))
            val comic = repository.fetchComic("").toList()
            assert(comic.first() is NetworkResource.Loading)
            assert(comic.last() is NetworkResource.Success)
            val data = (comic.last() as NetworkResource.Success).data
            assert(data?.title == comicTestData.dataContainer.results.first().title)
        }
    }

    @Test
    fun fetchComicByIdShouldReturnErrorData() {
        runBlocking {
            val responseBody = mock<ResponseBody>()
            val response: Response<ComicResponse> = Response.error(404, responseBody)
            whenever(networkService.getComic("")).thenReturn(response)
            val comic = repository.fetchComic("").toList()
            assert(comic.first() is NetworkResource.Loading)
            assert(comic.last() is NetworkResource.Error)
        }
    }

    private suspend fun <T : Any> PagingData<T>.collectData(): List<T> {
        val differCallback = object : DifferCallback {
            override fun onChanged(position: Int, count: Int) {}
            override fun onInserted(position: Int, count: Int) {}
            override fun onRemoved(position: Int, count: Int) {}
        }
        val items = mutableListOf<T>()
        val diff = object : PagingDataDiffer<T>(differCallback, TestCoroutineDispatcher()) {
            override suspend fun presentNewList(
                previousList: NullPaddedList<T>,
                newList: NullPaddedList<T>,
                newCombinedLoadStates: CombinedLoadStates,
                lastAccessedIndex: Int,
                onListPresentable: () -> Unit
            ): Int? {
                for (i in 0 until newList.size)
                    items.add(newList.getFromStorage(i))
                onListPresentable()
                return null
            }
        }
        diff.collectFrom(this)

        return items
    }
}
