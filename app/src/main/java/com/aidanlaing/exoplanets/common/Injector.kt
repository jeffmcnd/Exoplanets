package com.aidanlaing.exoplanets.common

import android.content.Context
import com.aidanlaing.exoplanets.BuildConfig
import com.aidanlaing.exoplanets.data.AppDatabase
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanetsDataSource
import com.aidanlaing.exoplanets.data.confirmedplanets.ConfirmedPlanetsRepo
import com.aidanlaing.exoplanets.data.confirmedplanets.local.ConfirmedPlanetsDao
import com.aidanlaing.exoplanets.data.confirmedplanets.local.ConfirmedPlanetsLocalDataSource
import com.aidanlaing.exoplanets.data.confirmedplanets.remote.ConfirmedPlanetsApi
import com.aidanlaing.exoplanets.data.confirmedplanets.remote.ConfirmedPlanetsRemoteDataSource
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.experimental.CoroutineContext

object Injector {

    private val cachedRetrofits = HashMap<String, Retrofit>()
    private var cachedOkHttpClient: OkHttpClient? = null

    fun provideViewModelFactory(
            context: Context,
            uiContext: CoroutineContext = UI,
            ioContext: CoroutineContext = CommonPool,
            confirmedPlanetsDataSource: ConfirmedPlanetsDataSource = provideConfirmedPlanetsRepo(context)
    ) = ViewModelFactory(
            uiContext,
            ioContext,
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

    fun provideAppDatabase(
            context: Context,
            name: String = "app.db"
    ): AppDatabase = AppDatabase.getInstance(context, name)

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