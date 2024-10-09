package com.githubsearchapp.common.utils.boundResource

import com.githubsearchapp.common.model.ApiError
import com.githubsearchapp.common.model.ApiWrapper
import com.githubsearchapp.common.model.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class HttpBoundResource<ResponseType, ResultType> : BaseBoundResource<ResponseType, ResultType>() {

    override fun isError(response: ResponseType?, result: ResultType?): ApiError? {
        return when (response) {
            is ApiWrapper.Error<*> -> ApiError(code = response.code, error = response.error)
            is ApiWrapper.UnknownError<*> -> ApiError(error = response.message)
            else -> null
        }
    }

    override fun processErrorResponse(response: ResponseType?, result: ResultType?) {}

    override fun saveNetworkResult(result: ResultType?) {}

    override fun fetchFromStorage(): ResultType? = null

    override fun determineFetchPolicy(): FetchPolicy = FetchPolicy.NETWORK_ONLY

    operator fun invoke() = flow {
        emit(Resource.Loading())
        val result: Resource<ResultType>
        val fetchPolicy = determineFetchPolicy()

        when (fetchPolicy) {
            FetchPolicy.CACHE_ONLY -> {
                val storageData = fetchFromStorage()
                result = Resource.Success(data = storageData)
            }

            FetchPolicy.NETWORK_ONLY -> {
                val networkData = fetchFromNetwork()
                val error = isError(networkData, null)
                if (error == null) {
                    val processedResponse = processResponse(networkData, null)
                    result = Resource.Success(processedResponse)
                } else {
                    processErrorResponse(networkData, null)
                    result = Resource.Error(error.error)
                }
            }

            FetchPolicy.NETWORK_AND_CACHE -> {
                val storageData = fetchFromStorage()
                val networkData = fetchFromNetwork()
                val error = isError(networkData, null)
                if (error == null) {
                    val processedResponse = processResponse(networkData, null)
                    saveNetworkResult(processedResponse)
                    result = Resource.Success(processedResponse)
                } else {
                    processErrorResponse(networkData, null)
                    result = Resource.Error(error.error, storageData)
                }
            }
        }
        emit(result)
    }.flowOn(Dispatchers.IO)
}