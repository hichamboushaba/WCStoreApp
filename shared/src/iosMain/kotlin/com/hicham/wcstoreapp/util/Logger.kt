package com.hicham.wcstoreapp.util

import org.koin.ext.getFullName
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSStringFromClass
import platform.darwin.NSObjectProtocol

class IOSLogger(private val minLogPriority: LogPriority = LogPriority.DEBUG) : Logger {
    private val dateFormatter = NSDateFormatter().apply {
        dateFormat = "MM-dd HH:mm:ss.SSS"
    }

    override fun isLoggable(priority: LogPriority): Boolean =
        priority.priorityInt >= minLogPriority.priorityInt

    override fun log(tag: String, priority: LogPriority, message: String) {
        val logMessage = buildString {
            append(dateFormatter.stringFromDate(NSDate()))
            append(" ")
            append(tag)
            append(" ")
            append(priority.name)
            append(" ")
            append(message)
        }
        println(logMessage)
    }
}

inline fun Any.log(
    message: () -> String
) = log(null, LogPriority.DEBUG, message)

@PublishedApi
internal actual fun Any.outerClassSimpleNameInternalOnlyDoNotUseKThxBye(): String {
    return if (this is NSObjectProtocol) {
        val className = this.`class`()?.let {
            NSStringFromClass(it)
        } ?: this.toString()
        className.substringAfterLast('.').removeSuffix("Kt")
    } else {
        val javaClass = this::class
        val fullClassName = javaClass.getFullName()
        val outerClassName = fullClassName.substringBefore('$')
        val simplerOuterClassName = outerClassName.substringAfterLast('.')
        if (simplerOuterClassName.isEmpty()) {
            fullClassName
        } else {
            simplerOuterClassName.removeSuffix("Kt")
        }
    }
}
