package com.githubsearchapp.common.utils.boundResource

import com.githubsearchapp.common.model.ApiError

abstract class BaseBoundResource<ResponseType, ResultType> {

    abstract fun determineFetchPolicy(): FetchPolicy

    open suspend fun fetchFromNetwork(): ResponseType? = null

    open fun fetchFromStorage(): ResultType? = null

    open fun isError(response: ResponseType?, result: ResultType?): ApiError? = null

    open fun processResponse(response: ResponseType?, resultType: ResultType?): ResultType? = null

    open fun processErrorResponse(response: ResponseType?, result: ResultType?) {}

    open fun saveNetworkResult(result: ResultType?) {}

}


enum class FetchPolicy {
    CACHE_ONLY, NETWORK_ONLY, NETWORK_AND_CACHE
}