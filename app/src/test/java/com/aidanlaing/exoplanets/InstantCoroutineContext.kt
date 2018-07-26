package com.aidanlaing.exoplanets

import kotlinx.coroutines.experimental.CancellableContinuation
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Delay
import java.util.concurrent.TimeUnit
import kotlin.coroutines.experimental.CoroutineContext

class InstantCoroutineContext : CoroutineDispatcher(), Delay {
    override fun scheduleResumeAfterDelay(
            time: Long,
            unit: TimeUnit,
            continuation: CancellableContinuation<Unit>
    ) = continuation.resume(Unit)

    override fun dispatch(context: CoroutineContext, block: Runnable) = block.run()
}