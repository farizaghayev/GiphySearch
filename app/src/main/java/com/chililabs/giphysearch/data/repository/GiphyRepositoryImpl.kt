package com.chililabs.giphysearch.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.chililabs.giphysearch.data.paging.GiphyPagingSource
import com.chililabs.giphysearch.data.remote.GiphyApi
import com.chililabs.giphysearch.domain.model.Gif
import com.chililabs.giphysearch.domain.repository.GiphyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GiphyRepositoryImpl @Inject constructor(
    private val api: GiphyApi
) : GiphyRepository {

    override fun searchGifs(query: String): Flow<PagingData<Gif>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                enablePlaceholders = false,
                initialLoadSize = 25
            ),
            pagingSourceFactory = { GiphyPagingSource(api, query) }
        ).flow
    }
}