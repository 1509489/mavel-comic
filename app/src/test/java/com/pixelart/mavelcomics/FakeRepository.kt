package com.pixelart.mavelcomics

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.pixelart.mavelcomics.datasource.NetworkResource
import com.pixelart.mavelcomics.datasource.network.NetworkService
import com.pixelart.mavelcomics.models.Comic
import com.pixelart.mavelcomics.repository.ComicsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository(private val networkService: NetworkService) : ComicsRepository {
    override fun fetchComics(): Flow<PagingData<Comic>> {
        val pageSource = FakeComicPageSource()
        return flow {
            val pageData = pageSource.load(PagingSource.LoadParams.Refresh(0, 4, false))
            val data = (pageData as PagingSource.LoadResult.Page).data
            emit(PagingData.from(data))
        }
    }

    override fun fetchComic(comicId: String): Flow<NetworkResource<Comic>> {
        return flow {
            emit(NetworkResource.Loading)
            try {
                val response = networkService.getComic(comicId)
                when {
                    response.isSuccessful -> {
                        emit(NetworkResource.Success(response.body()?.dataContainer?.results?.firstOrNull()))
                    }
                    else -> emit(NetworkResource.Error(Exception("Failed to fetch comic")))
                }
            } catch (ex: Exception) {
                emit(NetworkResource.Error(ex))
            }
        }
    }
}
