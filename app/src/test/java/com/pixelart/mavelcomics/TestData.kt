package com.pixelart.mavelcomics

import com.pixelart.mavelcomics.models.Comic
import com.pixelart.mavelcomics.models.ComicResponse
import com.pixelart.mavelcomics.models.DataContainer
import com.pixelart.mavelcomics.models.Thumbnail

val comicTestData = ComicResponse(
    attributionHTML = "",
    attributionText = "",
    code = "",
    copyright = "",
    dataContainer = DataContainer(
        count = "",
        limit = "",
        offset = "",
        total = "",
        results = listOf(
            Comic(
                id = "1",
                title = "Comic 1",
                description = "Comic 1 description",
                thumbnail = Thumbnail(
                    path = "",
                    extension = ""
                )
            ),
            Comic(
                id = "12",
                title = "Comic 2",
                description = "Comic 2 description",
                thumbnail = Thumbnail(
                    path = "",
                    extension = ""
                )
            ),
            Comic(
                id = "3",
                title = "Comic 3",
                description = "Comic 3 description",
                thumbnail = Thumbnail(
                    path = "",
                    extension = ""
                )
            ),
            Comic(
                id = "4",
                title = "Comic 4",
                description = "Comic 4 description",
                thumbnail = Thumbnail(
                    path = "",
                    extension = ""
                )
            )
        )
    ),
    etag = "",
    status = "",
)
