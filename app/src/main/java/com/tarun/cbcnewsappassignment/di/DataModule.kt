package com.tarun.cbcnewsappassignment.di

import androidx.room.Room
import com.tarun.cbcnewsappassignment.api.CBCNewsApiService
import com.tarun.cbcnewsappassignment.data.ArticleRepository
import com.tarun.cbcnewsappassignment.db.ArticleDatabase
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val CBC_API_BASE_URL = "https://www.cbc.ca/aggregate_api/v1/"
val dataModule = module {

    single { ArticleRepository() }

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

    single {
        synchronized(this) {
            Room.databaseBuilder(
                androidContext(),
                ArticleDatabase::class.java,
                "cbc_news_database"
            )
                // Wipes and rebuilds instead of migrating if no Migration object.
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    factory { get<ArticleDatabase>().articleDao() }
}