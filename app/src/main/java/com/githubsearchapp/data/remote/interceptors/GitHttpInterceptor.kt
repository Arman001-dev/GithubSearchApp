package com.githubsearchapp.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class GitHttpInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
            .newBuilder()
            .header("accept", "application/vnd.github+json")
            .build()
        return chain.proceed(request)
    }
}