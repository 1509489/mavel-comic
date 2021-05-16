package com.pixelart.mavelcomics.common

import com.pixelart.mavelcomics.models.Thumbnail

fun Thumbnail.toUrl(): String {
    return path.plus(".$extension")
}
