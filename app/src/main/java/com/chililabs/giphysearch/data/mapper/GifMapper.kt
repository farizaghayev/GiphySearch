package com.chililabs.giphysearch.data.mapper

import com.chililabs.giphysearch.data.remote.dto.GifDto
import com.chililabs.giphysearch.domain.model.Gif

fun GifDto.toDomain(): Gif {
    return Gif(
        id = id,
        title = title.ifEmpty { "Untitled" },
        username = username.ifEmpty { "Unknown" },
        rating = rating.uppercase(),
        previewUrl = images.fixedWidth.url,
        originalUrl = images.original.url,
        width = images.original.width.toIntOrNull() ?: 0,
        height = images.original.height.toIntOrNull() ?: 0,
        importDatetime = importDatetime
    )
}