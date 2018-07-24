package com.aidanlaing.exoplanets.common.livedata

import android.arch.lifecycle.Observer

class NonNullObserver<T>(val body: (t: T) -> Unit): Observer<T> {
    override fun onChanged(t: T?) {
        if (t != null) body(t)
    }
}