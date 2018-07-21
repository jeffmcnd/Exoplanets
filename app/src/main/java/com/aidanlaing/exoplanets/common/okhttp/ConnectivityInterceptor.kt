package com.aidanlaing.exoplanets.common.okhttp

import android.content.Context
import android.net.ConnectivityManager
import com.aidanlaing.exoplanets.common.exceptions.NoConnectionException
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        if (connectivityManager.activeNetworkInfo == null) {
            throw NoConnectionException()
        }

        return chain.proceed(chain.request())
    }

}