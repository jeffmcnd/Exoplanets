package com.aidanlaing.exoplanets.common.livedata

class SingleEvent<T>() {

    private var data: T? = null
    private var hasRun: Boolean = false

    constructor(data: T) : this() {
        this.data = data
    }

    fun invoke(body: () -> Unit) {
        if (!hasRun) {
            body()
            hasRun = true
        }
    }

    fun invokeWithData(body: (data: T) -> Unit) {
        if (!hasRun) {
            data?.let {
                body(it)
                hasRun = true
            }
        }
    }
}