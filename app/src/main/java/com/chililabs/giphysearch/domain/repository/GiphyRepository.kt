package com.chililabs.giphysearch.domain.repository

import androidx.paging.PagingData
import com.chililabs.giphysearch.domain.model.Gif
import kotlinx.coroutines.flow.Flow

interface GiphyRepository {
    fun searchGifs(query: String): Flow<PagingData<Gif>>
}