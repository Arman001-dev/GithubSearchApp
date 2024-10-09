package com.githubsearchapp.common.utils.boundResource

import com.githubsearchapp.common.model.ApiError

abstract class BaseBoundResource<ResponseType, ResultType> {

    abstract suspend fun fetchFromNetwork(): ResponseType?

    abstract fun fetchFromStorage(): ResultType?

    abstract fun isError(response: ResponseType?, result: ResultType?): ApiError?

    abstract fun processResponse(response: ResponseType?, resultType: ResultType?): ResultType?

    abstract fun processErrorResponse(response: ResponseType?, result: ResultType?)

    abstract fun saveNetworkResult(result: ResultType?)

    abstract fun determineFetchPolicy(): FetchPolicy
}


enum class FetchPolicy {
    CACHE_ONLY, NETWORK_ONLY, NETWORK_AND_CACHE
}