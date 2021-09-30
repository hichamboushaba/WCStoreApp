package com.hicham.wcstoreapp.ui.navigation

import androidx.lifecycle.asFlow
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationManager @Inject constructor() {

    private var navController: NavController? = null

    suspend fun handleNavigationCommands(navController: NavController) {
        navigationCommands
            .onSubscription { this@NavigationManager.navController = navController }
            .onCompletion { this@NavigationManager.navController = null }
            .collect {
                when (it) {
                    is NavigationCommand.NavigateToRoute -> navController.navigate(
                        it.route,
                        it.options
                    )
                    NavigationCommand.NavigateUp -> navController.navigateUp()
                    is NavigationCommand.PopUpToRoute -> navController.popBackStack(
                        it.route,
                        it.inclusive
                    )
                }
            }
    }

    private val navigationCommands = MutableSharedFlow<NavigationCommand>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    fun navigate(route: String) {
        navigationCommands.tryEmit(NavigationCommand.NavigateToRoute(route))
    }

    fun navigate(route: String, optionsBuilder: NavOptionsBuilder.() -> Unit) {
        val options = navOptions(optionsBuilder)
        navigationCommands.tryEmit(NavigationCommand.NavigateToRoute(route, options))
    }

    fun navigateUp() {
        navigationCommands.tryEmit(NavigationCommand.NavigateUp)
    }

    fun popUpTo(route: String, inclusive: Boolean = false) {
        navigationCommands.tryEmit(NavigationCommand.PopUpToRoute(route, inclusive))
    }

    fun <T> navigateBackWithResult(key: String, result: T, destination: String? = null) {
        val navController = navController ?: run {
            // shouldn't happen
            return
        }
        val backStackEntry = destination?.let { navController.getBackStackEntry(it) }
            ?: navController.previousBackStackEntry
        backStackEntry?.savedStateHandle?.set(key, result)

        destination?.let {
            navController.popBackStack(it, false)
        } ?: run {
            navController.navigateUp()
        }
    }

    fun <T> observeResult(key: String, route: String? = null): Flow<T> {
        val navController = navController ?: run {
            // shouldn't happen
            return emptyFlow()
        }

        val backStackEntry = route?.let { navController.getBackStackEntry(it) }
            ?: navController.currentBackStackEntry

        return backStackEntry?.savedStateHandle?.getLiveData<T>(key)?.asFlow()
            ?: emptyFlow()
    }
}

sealed class NavigationCommand {
    object NavigateUp : NavigationCommand()
    data class NavigateToRoute(val route: String, val options: NavOptions? = null) :
        NavigationCommand()

    data class PopUpToRoute(val route: String, val inclusive: Boolean) : NavigationCommand()
}