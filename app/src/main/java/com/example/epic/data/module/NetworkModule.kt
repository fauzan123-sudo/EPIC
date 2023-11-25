package com.example.epic.data.module

import com.example.epic.data.network.AuthApi
import com.example.epic.data.network.CategoryApi
import com.example.epic.data.network.HomeApi
import com.example.epic.data.network.ProductApi
import com.example.epic.data.network.ProductReturnApi
import com.example.epic.data.network.ProfileApi
import com.example.epic.data.network.ReportApi
import com.example.epic.data.network.SalesApi
import com.example.epic.data.network.SellerApi
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
import retrofit2.Retrofit.Builder
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Builder {
        val gson = GsonBuilder().setLenient().create()

        return Builder()
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
    fun providesUserAPI(retrofitBuilder: Builder, okHttpClient: OkHttpClient): AuthApi {
        return retrofitBuilder.client(okHttpClient).build()
            .create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun providesCategoryAPI(
        retrofitBuilder: Builder,
        okHttpClient: OkHttpClient
    ): CategoryApi {
        return retrofitBuilder.client(okHttpClient).build()
            .create(CategoryApi::class.java)
    }

    @Singleton
    @Provides
    fun providesProductAPI(
        retrofitBuilder: Builder,
        okHttpClient: OkHttpClient
    ): ProductApi {
        return retrofitBuilder.client(okHttpClient).build()
            .create(ProductApi::class.java)
    }

    @Singleton
    @Provides
    fun stockApi(retrofitBuilder: Builder, okHttpClient: OkHttpClient): StockApi {
        return retrofitBuilder.client(okHttpClient).build()
            .create(StockApi::class.java)
    }

    @Singleton
    @Provides
    fun productReturnApi(
        retrofitBuilder: Builder,
        okHttpClient: OkHttpClient
    ): ProductReturnApi {
        return retrofitBuilder.client(okHttpClient).build().create(ProductReturnApi::class.java)
    }

    @Singleton
    @Provides
    fun userManagementApi(
        retrofitBuilder: Builder,
        okHttpClient: OkHttpClient
    ): UserManagementApi {
        return retrofitBuilder.client(okHttpClient).build().create(UserManagementApi::class.java)
    }

    @Singleton
    @Provides
    fun salesApi(retrofitBuilder: Builder, okHttpClient: OkHttpClient): SalesApi {
        return retrofitBuilder.client(okHttpClient).build().create(SalesApi::class.java)
    }

    @Singleton
    @Provides
    fun sellerApi(retrofitBuilder: Builder, okHttpClient: OkHttpClient): SellerApi {
        return retrofitBuilder.client(okHttpClient).build().create(SellerApi::class.java)
    }

    @Singleton
    @Provides
    fun profileApi(retrofitBuilder: Builder, okHttpClient: OkHttpClient): ProfileApi {
        return retrofitBuilder.client(okHttpClient).build().create(ProfileApi::class.java)
    }

    @Singleton
    @Provides
    fun reportApi(retrofitBuilder: Builder, okHttpClient: OkHttpClient): ReportApi {
        return retrofitBuilder.client(okHttpClient).build().create(ReportApi::class.java)
    }

    @Singleton
    @Provides
    fun homeApi(retrofitBuilder: Builder, okHttpClient: OkHttpClient): HomeApi {
        return retrofitBuilder.client(okHttpClient).build().create(HomeApi::class.java)
    }
}