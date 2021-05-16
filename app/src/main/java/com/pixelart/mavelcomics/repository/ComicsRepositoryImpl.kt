package com.pixelart.mavelcomics.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pixelart.mavelcomics.datasource.ComicPagingSource
import com.pixelart.mavelcomics.datasource.NetworkResource
import com.pixelart.mavelcomics.datasource.network.NetworkService
import com.pixelart.mavelcomics.models.Comic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ComicsRepositoryImpl @Inject constructor(
    private val networkService: NetworkService
) : ComicsRepository {
    companion object {
        private const val PAGE_SIZE = 20
    }

    override fun fetchComics(): Flow<PagingData<Comic>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ComicPagingSource(networkService) }
        ).flow
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
                    else -> emit(NetworkResource.Error(Exception(response.errorBody()?.toString())))
                }
            } catch (ex: Exception) {
                emit(NetworkResource.Error(ex))
            }
        }.catch {  }
    }
}
