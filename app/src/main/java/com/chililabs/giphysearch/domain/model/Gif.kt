package com.chililabs.giphysearch.domain.model

data class Gif(
    val id: String,
    val title: String,
    val username: String,
    val rating: String,
    val previewUrl: String,
    val originalUrl: String,
    val width: Int,
    val height: Int,
    val importDatetime: String
)