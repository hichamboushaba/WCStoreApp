package com.hicham.wcstoreapp.ui

import kotlinx.coroutines.flow.Flow

interface NavigationManager {
    fun navigate(route: String)
    fun navigateUp()
    fun popUpTo(route: String, inclusive: Boolean = false)
    fun <T> navigateBackWithResult(
        key: String,
        result: T,
        destination: String? = null
    )
    fun <T> observeResult(key: String, route: String? = null): Flow<T>
}