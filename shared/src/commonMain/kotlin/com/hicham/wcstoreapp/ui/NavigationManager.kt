package com.hicham.wcstoreapp.ui

interface NavigationManager {
    fun navigate(route: String)
    fun navigateUp()
    fun popUpTo(route: String, inclusive: Boolean = false)
}