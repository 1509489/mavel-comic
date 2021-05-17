package com.pixelart.mavelcomics

import androidx.paging.PagingSource
import com.pixelart.mavelcomics.datasource.ComicPagingSource
import com.pixelart.mavelcomics.datasource.network.NetworkService
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ComicPagingSourceTest {
    private val networkService: NetworkService = mock()

    private lateinit var pageSource: ComicPagingSource

    @Before
    fun setup() {
        pageSource = ComicPagingSource(networkService)
    }

    @Test
    fun `Get Comic List Should Fetch From API`() {
        val params: PagingSource.LoadParams<Int> = mock()

        runBlocking {
            whenever(networkService.getComics()).thenReturn(comicTestData)
            pageSource.load(params)
            verify(networkService).getComics()
        }
    }
}
