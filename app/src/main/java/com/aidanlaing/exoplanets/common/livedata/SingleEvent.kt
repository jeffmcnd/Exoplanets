package com.aidanlaing.exoplanets.common.livedata

class SingleEvent {

    private var handled: Boolean = false

    fun invokeIfNotHandled(body: () -> Unit) {
        if (!handled) {
            body()
            handled = true
        }
    }
}