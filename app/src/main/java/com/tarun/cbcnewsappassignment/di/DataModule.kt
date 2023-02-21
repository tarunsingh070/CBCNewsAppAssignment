package com.tarun.cbcnewsappassignment.di

import com.tarun.cbcnewsappassignment.api.CBCNewsApiService
import com.tarun.cbcnewsappassignment.data.CBCNewsRepository
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val CBC_API_BASE_URL = "https://www.cbc.ca/aggregate_api/v1/"
val dataModule = module {

    single { CBCNewsRepository() }

    single<CBCNewsApiService> { get<Retrofit>().create(CBCNewsApiService::class.java) }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(CBC_API_BASE_URL)
            .client(get())
            .addConverterFactory(get<GsonConverterFactory>())
            .build()
    }

    single<OkHttpClient> {
        OkHttpClient.Builder().build()
    }

    single { GsonConverterFactory.create() }
}