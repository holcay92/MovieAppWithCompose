package com.example.fourthday.di

import com.example.fourthday.Constants
import com.example.fourthday.service.PokeApiService
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
    fun logging() :HttpLoggingInterceptor{
       return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    @Provides
     fun client(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging())
            .connectTimeout(20, TimeUnit.SECONDS)// modified for long time request
            .readTimeout(20, TimeUnit.SECONDS)// modified for long time request
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
    fun pokeApiService(): PokeApiService {
        return retrofit().create(PokeApiService::class.java)
    }
}