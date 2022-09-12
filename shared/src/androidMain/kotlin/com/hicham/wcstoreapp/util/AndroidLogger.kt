package com.hicham.wcstoreapp.util

import android.os.Build
import android.util.Log
import org.koin.ext.getFullName
import kotlin.math.min

private const val MAX_LOG_LENGTH = 4000
private const val MAX_TAG_LENGTH = 23

class AndroidLogcatLogger(private val minLogPriority: LogPriority = LogPriority.DEBUG) : Logger {
    override fun isLoggable(priority: LogPriority): Boolean =
        priority.priorityInt >= minLogPriority.priorityInt

    override fun log(tag: String, priority: LogPriority, message: String) {
        val actualTag = tag

        // Tag length limit was removed in API 26.
        val trimmedTag = if (tag.length <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= 26) {
            tag
        } else {
            tag.substring(0, MAX_TAG_LENGTH)
        }

        if (message.length < MAX_LOG_LENGTH) {
            logToLogcat(priority.priorityInt, trimmedTag, message)
            return
        }

        // Split by line, then ensure each line can fit into Log's maximum length.
        var i = 0
        val length = message.length
        while (i < length) {
            var newline = message.indexOf('\n', i)
            newline = if (newline != -1) newline else length
            do {
                val end = min(newline, i + MAX_LOG_LENGTH)
                val part = message.substring(i, end)
                logToLogcat(priority.priorityInt, trimmedTag, part)
                i = end
            } while (i < newline)
            i++
        }
    }

    private fun logToLogcat(priority: Int, tag: String, part: String) {
        if (priority == Log.ASSERT) {
            Log.wtf(tag, part)
        } else {
            Log.println(priority, tag, part)
        }
    }
}

@PublishedApi
internal actual fun Any.outerClassSimpleNameInternalOnlyDoNotUseKThxBye(): String {
    val javaClass = this::class
    val fullClassName = javaClass.getFullName()
    val outerClassName = fullClassName.substringBefore('$')
    val simplerOuterClassName = outerClassName.substringAfterLast('.')
    return if (simplerOuterClassName.isEmpty()) {
        fullClassName
    } else {
        simplerOuterClassName.removeSuffix("Kt")
    }
}
