package com.githubsearchapp.data.di

import android.content.Context
import androidx.room.Room
import com.githubsearchapp.common.utils.Constants.GIT_API_BASE_URL
import com.githubsearchapp.data.local.dao.GitReposDao
import com.githubsearchapp.data.local.database.GitReposDatabase
import com.githubsearchapp.data.remote.GitNetworkAdapter
import com.githubsearchapp.data.remote.GitNetworkPort
import com.githubsearchapp.data.remote.apiservice.GitApiService
import com.githubsearchapp.data.remote.interceptors.GitHttpInterceptor
import com.githubsearchapp.data.repository.GitRepositoryImpl
import com.githubsearchapp.domain.repository.GitRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InterceptorModule {

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
    }

    @Provides
    fun provideGitHttpInterceptor(): GitHttpInterceptor {
        return GitHttpInterceptor()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object HttpClientModule {

    @Provides
    fun provideHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor, gitHttpInterceptor: GitHttpInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(gitHttpInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {

    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    fun provideGitApiService(okHttpClient: OkHttpClient): GitApiService {
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        }
        return Retrofit.Builder()
            .baseUrl(GIT_API_BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()
            .create(GitApiService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindGitNetworkPort(gitNetworkAdapter: GitNetworkAdapter): GitNetworkPort

    @Binds
    abstract fun bindGitRepository(gitRepository: GitRepositoryImpl): GitRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideGitReposDatabase(@ApplicationContext appContext: Context): GitReposDatabase {
        return Room.databaseBuilder(
            appContext,
            GitReposDatabase::class.java,
            "room_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(appDatabase: GitReposDatabase): GitReposDao {
        return appDatabase.gitReposDao()
    }
}
