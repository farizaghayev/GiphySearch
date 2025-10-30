package com.chililabs.giphysearch.domain.usecase

import androidx.paging.PagingData
import com.chililabs.giphysearch.domain.model.Gif
import com.chililabs.giphysearch.domain.repository.GiphyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchGifsUseCase @Inject constructor(
    private val repository: GiphyRepository
) {
    operator fun invoke(query: String): Flow<PagingData<Gif>> {
        return repository.searchGifs(query)
    }
}