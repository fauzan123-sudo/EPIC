package com.example.epic.data.module

import com.example.epic.data.repository.MyAuthRepository
import com.example.epic.data.repository.impl.MyAuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun providesAuthRepository(impl: MyAuthRepositoryImpl): MyAuthRepository = impl

}