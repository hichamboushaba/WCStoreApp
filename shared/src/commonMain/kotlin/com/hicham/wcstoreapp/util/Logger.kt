package com.hicham.wcstoreapp.util

/**
 * A basic shared logging functionality inspired by Square's [LogCat](https://github.com/square/logcat).
 */
inline fun Any.log(
    tag: String? = null,
    priority: LogPriority = LogPriority.DEBUG,
    message: () -> String
) {
    Logger.logger.let { logger ->
        if (logger.isLoggable(priority)) {
            val actualTag = tag ?: outerClassSimpleNameInternalOnlyDoNotUseKThxBye()
            logger.log(actualTag, priority, message())
        }
    }
}

inline fun Any.log(
    priority: LogPriority = LogPriority.DEBUG,
    message: () -> String
) = log(null, priority, message)

@PublishedApi
internal expect fun Any.outerClassSimpleNameInternalOnlyDoNotUseKThxBye(): String

interface Logger {
    fun isLoggable(priority: LogPriority): Boolean = true
    fun log(tag: String, priority: LogPriority, message: String)

    companion object {
        var logger: Logger = NoLog()
            private set

        fun installLogger(logger: Logger) {
            this.logger = logger
        }
    }
}

private class NoLog : Logger {
    override fun isLoggable(priority: LogPriority): Boolean = false
    override fun log(tag: String, priority: LogPriority, message: String) =
        error("Shouldn't reach here")
}

enum class LogPriority(
    val priorityInt: Int
) {
    VERBOSE(2),
    DEBUG(3),
    INFO(4),
    WARN(5),
    ERROR(6),
    ASSERT(7);
}
