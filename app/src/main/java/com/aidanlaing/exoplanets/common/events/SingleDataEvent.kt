package com.aidanlaing.exoplanets.common.events

class SingleDataEvent<T>(val data: T) {

    private var handled: Boolean = false

    fun invokeIfNotHandled(body: (data: T) -> Unit) {
        if (!handled) {
            body(data)
            handled = true
        }
    }

}