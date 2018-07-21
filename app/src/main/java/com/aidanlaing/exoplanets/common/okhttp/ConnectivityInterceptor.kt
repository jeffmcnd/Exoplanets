package com.aidanlaing.exoplanets.common.okhttp

import android.content.Context
import com.aidanlaing.exoplanets.common.exceptions.NoConnectionException
import com.aidanlaing.exoplanets.common.extensions.isNetworkAvailable
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        if (!context.isNetworkAvailable()) {
            throw NoConnectionException()
        }

        return chain.proceed(chain.request())
    }

}