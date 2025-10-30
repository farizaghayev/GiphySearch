package com.chililabs.giphysearch.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.chililabs.giphysearch.BuildConfig
import com.chililabs.giphysearch.data.mapper.toDomain
import com.chililabs.giphysearch.data.remote.GiphyApi
import com.chililabs.giphysearch.domain.model.Gif
import retrofit2.HttpException
import java.io.IOException

class GiphyPagingSource(
    private val api: GiphyApi,
    private val query: String
) : PagingSource<Int, Gif>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gif> {
        val position = params.key ?: 0

        return try {
            val response = if (query.isBlank()) {
                api.getTrendingGifs(
                    apiKey = BuildConfig.GIPHY_API_KEY,
                    limit = params.loadSize,
                    offset = position
                )
            } else {
                api.searchGifs(
                    apiKey = BuildConfig.GIPHY_API_KEY,
                    query = query,
                    limit = params.loadSize,
                    offset = position
                )
            }

            val gifs = response.data.map { it.toDomain() }
            val nextKey = if (gifs.isEmpty()) {
                null
            } else {
                position + params.loadSize
            }

            LoadResult.Page(
                data = gifs,
                prevKey = if (position == 0) null else position - params.loadSize,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Gif>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(state.config.pageSize)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(state.config.pageSize)
        }
    }
}