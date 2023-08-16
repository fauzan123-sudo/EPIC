package com.example.epic.data.module

import com.example.epic.data.network.AuthApi
import com.example.epic.util.AuthInterceptor
import com.example.epic.util.Constans
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit.Builder {
        val gson = GsonBuilder().setLenient().create()

        return Retrofit.Builder()
            .baseUrl(Constans.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun providesUserAPI(retrofitBuilder: Retrofit.Builder): AuthApi {
        return retrofitBuilder.build()
            .create(AuthApi::class.java)
    }

//    @Singleton
//    @Provides
//    fun providesHomeAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): HomeApi {
//        return retrofitBuilder.client(okHttpClient).build().create(HomeApi::class.java)
//    }
}