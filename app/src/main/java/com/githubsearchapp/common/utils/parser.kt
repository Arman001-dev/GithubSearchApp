package com.githubsearchapp.common.utils

import com.githubsearchapp.common.model.ApiWrapper
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import retrofit2.Response
import kotlin.reflect.KClass


suspend inline fun <reified T> parseResponse(crossinline apiFunction: suspend () -> Response<T>): ApiWrapper<T> {
    return try {
        val response = apiFunction.invoke()
        if (response.isSuccessful) {
            ApiWrapper.Success(response.body())
        } else {
            ApiWrapper.Error(response.code(), response.errorBody()?.toString())
        }
    } catch (e: Exception) {
        ApiWrapper.UnknownError(e.message)
    }
}

val json = Json {
    ignoreUnknownKeys = true
}

@OptIn(InternalSerializationApi::class)
fun <E : Any> parseErrorBody(errorBody: String, clazz: KClass<E>): E? {
    return try {
        json.decodeFromString(clazz.serializer(), errorBody)
    } catch (e: Exception) {
        null
    }
}