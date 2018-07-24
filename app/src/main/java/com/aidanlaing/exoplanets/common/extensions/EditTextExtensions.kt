package com.aidanlaing.exoplanets.common.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.ReceiveChannel

fun EditText.onTextChanged(): ReceiveChannel<String> = Channel<String>(capacity = Channel.UNLIMITED)
        .also { channel ->
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(editable: Editable?) {
                    editable?.toString().orEmpty().let(channel::offer)
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })
        }