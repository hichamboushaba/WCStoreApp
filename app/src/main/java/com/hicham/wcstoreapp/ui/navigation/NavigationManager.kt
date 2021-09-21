package com.hicham.wcstoreapp.ui

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

    fun navigateUp() {
        _navigationCommands.tryEmit(NavigationCommand.NavigateUp)
    }
}

sealed class NavigationCommand {
    object NavigateUp : NavigationCommand()
    data class NavigateToRoute(val route: String) : NavigationCommand()
}