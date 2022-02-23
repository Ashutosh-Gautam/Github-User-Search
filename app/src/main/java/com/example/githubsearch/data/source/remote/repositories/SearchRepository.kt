package com.example.githubsearch.data.source.remote.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.githubsearch.api.ApiInterface
import com.example.githubsearch.api.ApiRequest
import com.example.githubsearch.api.Result
import com.example.githubsearch.data.model.SearchDataResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepository @Inject constructor(private val service: ApiInterface) : ApiRequest() {

    fun getSearchResultStream(query: String): Flow<PagingData<SearchDataResponse.Items>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { UserPagingSource(service, query, NETWORK_PAGE_SIZE) }
        ).flow
    }

    suspend fun fetchUserDetail(login: String): Result<SearchDataResponse.Items> {
        return apiRequest { service.fetchUserDetail(login) }
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }
}