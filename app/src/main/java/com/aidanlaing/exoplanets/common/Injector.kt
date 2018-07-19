package com.aidanlaing.exoplanets.common

import android.content.Context
import com.aidanlaing.exoplanets.BuildConfig
import com.aidanlaing.exoplanets.data.AppDatabase
import com.aidanlaing.exoplanets.data.planets.PlanetsDataSource
import com.aidanlaing.exoplanets.data.planets.PlanetsRepo
import com.aidanlaing.exoplanets.data.planets.local.PlanetsDao
import com.aidanlaing.exoplanets.data.planets.local.PlanetsLocalDataSource
import com.aidanlaing.exoplanets.data.planets.remote.PlanetsApi
import com.aidanlaing.exoplanets.data.planets.remote.PlanetsRemoteDataSource
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
            planetsDataSource: PlanetsDataSource = providePlanetsRepo(context)
    ) = ViewModelFactory(
            uiContext,
            ioContext,
            planetsDataSource
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

    fun providePlanetsRepo(
            context: Context,
            planetsApi: PlanetsApi = providePlanetsApi(),
            planetsDao: PlanetsDao = providePlanetsDao(context)
    ): PlanetsRepo = PlanetsRepo.getInstance(
            PlanetsRemoteDataSource.getInstance(planetsApi),
            PlanetsLocalDataSource.getInstance(planetsDao)
    )

    fun providePlanetsApi(
            retrofit: Retrofit = provideRetrofit()
    ): PlanetsApi = retrofit.create(PlanetsApi::class.java)

    fun providePlanetsDao(
            context: Context,
            appDatabase: AppDatabase = provideAppDatabase(context)
    ): PlanetsDao = appDatabase.planetsDao()

}