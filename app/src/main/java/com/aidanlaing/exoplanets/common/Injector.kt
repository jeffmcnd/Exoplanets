package com.aidanlaing.exoplanets.common

import android.content.Context
import com.aidanlaing.exoplanets.BuildConfig
import com.aidanlaing.exoplanets.data.AppDatabase
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanetsApi
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanetsDao
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanetsDataSource
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanetsLocalDataSource
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanetsRemoteDataSource
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanetsRepo
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Injector {

    private val cachedRetrofits = HashMap<String, Retrofit>()
    private var cachedOkHttpClient: OkHttpClient? = null

    fun provideViewModelFactory(
            context: Context,
            confirmedPlanetsDataSource: ConfirmedPlanetsDataSource = provideConfirmedPlanetsRepo(context)
    ) = ViewModelFactory(
            confirmedPlanetsDataSource
    )

    fun provideRetrofit(
            baseUrl: String = BuildConfig.EXOPLANET_BASE_URL,
            okHttpClient: OkHttpClient = provideOkHttpClient(),
            converterFactory: Converter.Factory = GsonConverterFactory.create(),
            callAdapterFactory: CallAdapter.Factory = CoroutineCallAdapterFactory.invoke()
    ): Retrofit = cachedRetrofits[baseUrl] ?: Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(callAdapterFactory)
            .build()
            .also { retrofit -> cachedRetrofits[baseUrl] = retrofit }

    fun provideOkHttpClient(): OkHttpClient = cachedOkHttpClient ?: OkHttpClient.Builder()
            .build()
            .also { okHttpClient -> cachedOkHttpClient = okHttpClient }

    fun provideAppDatabase(context: Context): AppDatabase = AppDatabase.getInstance(context)

    fun provideConfirmedPlanetsRepo(
            context: Context,
            confirmedPlanetsApi: ConfirmedPlanetsApi = provideConfirmedPlanetsApi(),
            confirmedPlanetsDao: ConfirmedPlanetsDao = provideConfirmedPlanetsDao(context)
    ): ConfirmedPlanetsRepo = ConfirmedPlanetsRepo.getInstance(
            ConfirmedPlanetsRemoteDataSource.getInstance(confirmedPlanetsApi),
            ConfirmedPlanetsLocalDataSource.getInstance(confirmedPlanetsDao)
    )

    fun provideConfirmedPlanetsApi(
            retrofit: Retrofit = provideRetrofit()
    ): ConfirmedPlanetsApi = retrofit.create(ConfirmedPlanetsApi::class.java)

    fun provideConfirmedPlanetsDao(
            context: Context,
            appDatabase: AppDatabase = provideAppDatabase(context)
    ): ConfirmedPlanetsDao = appDatabase.confirmedPlanetsDao()

}