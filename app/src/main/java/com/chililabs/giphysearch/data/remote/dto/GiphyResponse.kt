package com.chililabs.giphysearch.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GiphyResponse(
    @SerializedName("data")
    val data: List<GifDto>,
    @SerializedName("pagination")
    val pagination: Pagination
)

data class GifDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("username")
    val username: String = "",
    @SerializedName("rating")
    val rating: String,
    @SerializedName("images")
    val images: Images,
    @SerializedName("import_datetime")
    val importDatetime: String
)

data class Images(
    @SerializedName("fixed_width")
    val fixedWidth: ImageData,
    @SerializedName("original")
    val original: ImageData
)

data class ImageData(
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: String,
    @SerializedName("height")
    val height: String
)

data class Pagination(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("count")
    val count: Int,
    @SerializedName("offset")
    val offset: Int
)