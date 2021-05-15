package com.pixelart.mavelcomics.datasource.network

import com.pixelart.mavelcomics.models.ComicResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkService {
    @GET("v1/public/comics")
    suspend fun getComics(
        @Query("offset") offset: Int = 0
    ): ComicResponse

    @GET("v1/public/comics/{comicId}")
    suspend fun getComic(
        @Path("comicId") comicId: String
    ): Response<ComicResponse>
}
