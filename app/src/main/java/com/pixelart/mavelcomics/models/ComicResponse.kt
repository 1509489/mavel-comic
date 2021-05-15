package com.pixelart.mavelcomics.models

import com.google.gson.annotations.SerializedName

data class ComicResponse(
    val attributionHTML: String,
    val attributionText: String,
    val code: String,
    val copyright: String,
    @SerializedName("data")
    val dataContainer: DataContainer,
    val etag: String,
    val status: String
)

data class DataContainer(
    val count: String,
    val limit: String,
    val offset: String,
    val results: List<Comic>,
    val total: String
)

data class Comic(
    val description: String,
    val id: String,
    val thumbnail: Thumbnail,
    val title: String
)

data class Thumbnail(
    val extension: String,
    val path: String
)
