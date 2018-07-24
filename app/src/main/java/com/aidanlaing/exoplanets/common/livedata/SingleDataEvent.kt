package com.aidanlaing.exoplanets.common.livedata

class SingleDataEvent<T>(private var data: T) {

    private var handled: Boolean = false

    fun invokeIfNotHandled(body: (data: T) -> Unit) {
        if (!handled) {
            body(data)
            handled = true
        }
    }

}