package com.pixelart.mavelcomics.repository

import androidx.paging.PagingData
import com.pixelart.mavelcomics.datasource.NetworkResource
import com.pixelart.mavelcomics.models.Comic
import kotlinx.coroutines.flow.Flow

interface ComicsRepository {
    fun fetchComics() : Flow<PagingData<Comic>>
    fun fetchComic(comicId: String): Flow<NetworkResource<Comic>>
}
