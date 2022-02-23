package com.example.githubsearch.data.source.remote.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubsearch.api.ApiInterface
import com.example.githubsearch.data.model.SearchDataResponse

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

class UserPagingSource(
    private val service: ApiInterface,
    private val query: String,
    private val NETWORK_PAGE_SIZE: Int
) : PagingSource<Int, SearchDataResponse.Items>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchDataResponse.Items> {
        val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        return try {
            val response = service.fetchGithubUsers(query, page, params.loadSize)
            val users = response.items
            LoadResult.Page(
                data = users,
                prevKey = if (page == UNSPLASH_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page == response.total_count/NETWORK_PAGE_SIZE) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchDataResponse.Items>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}