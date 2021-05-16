package com.pixelart.mavelcomics.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pixelart.mavelcomics.datasource.network.NetworkService
import com.pixelart.mavelcomics.models.Comic
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ComicPagingSource @Inject constructor(
    private val networkService: NetworkService
): PagingSource<Int, Comic>() {
    private val dataOffset = 20

    override fun getRefreshKey(state: PagingState<Int, Comic>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(dataOffset)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.plus(dataOffset)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comic> {
        val offset = params.key ?: 0

        return try {
            val response = networkService.getComics(offset)
            val comics = response.dataContainer.results
            LoadResult.Page(
                data = comics,
                nextKey = if (comics.isEmpty()) null else offset + dataOffset,
                prevKey = if (offset == 0) null else offset - dataOffset
            )
        } catch (ex: IOException) {
            return LoadResult.Error(ex)
        } catch (ex: HttpException) {
            return LoadResult.Error(ex)
        }
    }
}
