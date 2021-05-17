package com.pixelart.mavelcomics

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pixelart.mavelcomics.models.Comic

class FakeComicPageSource : PagingSource<Int, Comic>() {
    var isError: Boolean = false
    override fun getRefreshKey(state: PagingState<Int, Comic>): Int? {
        return state.anchorPosition ?: 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comic> {
        if (isError) {
            return LoadResult.Error(Throwable("Error occurred"))
        }
        return LoadResult.Page(
            data = comicTestData.dataContainer.results,
            prevKey = null,
            nextKey = null
        )
    }
}
