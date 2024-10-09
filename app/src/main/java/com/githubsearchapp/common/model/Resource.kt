package com.githubsearchapp.common.model

enum class ResourceStatusEnum {
    SUCCESS, ERROR, LOADING
}

sealed class Resource<T>(
    resourceStatusEnum: ResourceStatusEnum,
    data: T? = null,
    error: String? = null
) {
    data class Success<T>(val data: T?) : Resource<T>(resourceStatusEnum = ResourceStatusEnum.SUCCESS, data = data)

    data class Error<T>(val error: String?,val data: T? = null) : Resource<T>(resourceStatusEnum = ResourceStatusEnum.ERROR, error = error)

    class Loading<T> : Resource<T>(resourceStatusEnum = ResourceStatusEnum.LOADING)
}