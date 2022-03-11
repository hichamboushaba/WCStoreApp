package com.hicham.wcstoreapp.util

import com.kuuurt.paging.multiplatform.helpers.dispatcher
import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun <T> Flow<T>.wrap(): CFlow<T> = CFlow(this)

class CFlow<T>(private val origin: Flow<T>) : Flow<T> by origin {
    fun watch(block: (T) -> Unit): Closeable {
        val job = Job(/*ConferenceService.coroutineContext[Job]*/)

        onEach {
            block(it)
        }.launchIn(CoroutineScope(dispatcher() + job))

        return object : Closeable {
            override fun close() {
                job.cancel()
            }
        }
    }
}
