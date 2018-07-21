package com.aidanlaing.exoplanets.common.extensions

import android.content.Context
import android.net.ConnectivityManager

fun Context?.isNetworkAvailable(): Boolean {
    val connectivityManager = this?.getSystemService(Context.CONNECTIVITY_SERVICE)
            as? ConnectivityManager

    return connectivityManager?.activeNetworkInfo != null
}