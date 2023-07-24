package com.example.movieapp.di

import com.example.movieapp.Constants
import com.example.movieapp.Constants.API_KEY
import com.example.movieapp.Constants.BEARER
import com.example.movieapp.service.MovieApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Provides
    fun logging(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun client(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $BEARER")
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(20, TimeUnit.SECONDS) // modified for long time request
            .readTimeout(20, TimeUnit.SECONDS) // modified for long time request
            .build()
    }

    @Provides
    fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun movieApiService(): MovieApiService {
        return retrofit().create(MovieApiService::class.java)
    }
}
