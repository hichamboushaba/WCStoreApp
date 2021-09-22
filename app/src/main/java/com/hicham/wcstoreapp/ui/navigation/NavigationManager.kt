package com.hicham.wcstoreapp.ui.navigation

import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationManager @Inject constructor() {

    private val _navigationCommands = MutableSharedFlow<NavigationCommand>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigationCommands = _navigationCommands.asSharedFlow()

    fun navigate(route: String) {
        _navigationCommands.tryEmit(NavigationCommand.NavigateToRoute(route))
    }

    fun navigate(route: String, optionsBuilder: NavOptionsBuilder.() -> Unit) {
        val options = navOptions(optionsBuilder)
        _navigationCommands.tryEmit(NavigationCommand.NavigateToRoute(route, options))
    }

    fun navigateUp() {
        _navigationCommands.tryEmit(NavigationCommand.NavigateUp)
    }

    fun popUpTo(route: String, inclusive: Boolean = false) {
        _navigationCommands.tryEmit(NavigationCommand.PopUpToRoute(route, inclusive))
    }
}

sealed class NavigationCommand {
    object NavigateUp : NavigationCommand()
    data class NavigateToRoute(val route: String, val options: NavOptions? = null) :
        NavigationCommand()

    data class PopUpToRoute(val route: String, val inclusive: Boolean) : NavigationCommand()
}