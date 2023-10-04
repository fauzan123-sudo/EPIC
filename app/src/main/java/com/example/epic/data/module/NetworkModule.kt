package com.example.epic.data.module

import com.example.epic.data.network.AuthApi
import com.example.epic.data.network.CategoryApi
import com.example.epic.data.network.ProductApi
import com.example.epic.data.network.ProductReturnApi
import com.example.epic.data.network.SalesApi
import com.example.epic.data.network.StockApi
import com.example.epic.data.network.UserManagementApi
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
import retrofit2.Retrofit.Builder
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
//            .addConverterFactory(GsonConverterFactory.create(gson))
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
    fun providesUserAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): AuthApi {
        return retrofitBuilder.client(okHttpClient).build()
            .create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun providesCategoryAPI(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): CategoryApi {
        return retrofitBuilder.client(okHttpClient).build()
            .create(CategoryApi::class.java)
    }

    @Singleton
    @Provides
    fun providesProductAPI(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): ProductApi {
        return retrofitBuilder.client(okHttpClient).build()
            .create(ProductApi::class.java)
    }

    @Singleton
    @Provides
    fun stockApi(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): StockApi {
        return retrofitBuilder.client(okHttpClient).build()
            .create(StockApi::class.java)
    }

    @Singleton
    @Provides
    fun productReturnApi(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): ProductReturnApi {
        return retrofitBuilder.client(okHttpClient).build().create(ProductReturnApi::class.java)
    }

    @Singleton
    @Provides
    fun userManagementApi(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): UserManagementApi {
        return retrofitBuilder.client(okHttpClient).build().create(UserManagementApi::class.java)
    }

    @Singleton
    @Provides
    fun salesApi(retrofitBuilder: Builder, okHttpClient: OkHttpClient): SalesApi {
        return retrofitBuilder.client(okHttpClient).build().create(SalesApi::class.java)
    }

}