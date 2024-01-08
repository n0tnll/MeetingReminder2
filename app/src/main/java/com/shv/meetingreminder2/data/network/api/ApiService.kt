package com.shv.meetingreminder2.data.network.api

import com.shv.meetingreminder2.data.network.models.ClientListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/")
    suspend fun getClientsList(
        @Query(QUERY_PARAM_INC) include: String = PARAMS_INCLUDE,
        @Query(QUERY_PARAM_NOINFO) noinfo: String = CONTAIN_INFO,
        @Query(QUERY_PARAM_RESULTS) results: Int = LIMIT_RESULTS
    ): ClientListDto

    companion object {
        private const val QUERY_PARAM_INC = "inc"
        private const val QUERY_PARAM_NOINFO = "noinfo"
        private const val QUERY_PARAM_RESULTS = "results"

        private const val PARAMS_INCLUDE = "name,email,picture"
        private const val CONTAIN_INFO = "noinfo"
        private const val LIMIT_RESULTS = 15
    }
}