package com.aidanlaing.exoplanets.common.glide

import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class GlideListener<T>(
        private val onSuccess: () -> Unit,
        private val onError: () -> Unit
) : RequestListener<T> {

    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<T>?, isFirstResource: Boolean): Boolean {
        onSuccess()
        return false
    }

    override fun onResourceReady(resource: T, model: Any?, target: Target<T>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
        onError()
        return false
    }
}