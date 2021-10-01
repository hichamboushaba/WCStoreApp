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
    // We use a StateFlow here to allow ViewModels to start observing navigation results before the initial composition,
    // and still get the navigation result later
    private val navControllerFlow = MutableStateFlow<NavController?>(null)

    suspend fun handleNavigationCommands(navController: NavController) {
        navigationCommands
            .onSubscription { this@NavigationManager.navControllerFlow.value = navController }
            .onCompletion { this@NavigationManager.navControllerFlow.value = null }
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
        val navController = navControllerFlow.value ?: run {
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
        return navControllerFlow
            .filterNotNull()
            .flatMapLatest { navController ->
                val backStackEntry = route?.let { navController.getBackStackEntry(it) }
                    ?: navController.currentBackStackEntry

                backStackEntry?.savedStateHandle?.let { savedStateHandle ->
                    savedStateHandle.getLiveData<T?>(key)
                        .asFlow()
                        .filter { it != null }
                        .onEach {
                            // Nullify the result to avoid resubmitting it
                            savedStateHandle.set(key, null)
                        }
                } ?: emptyFlow()
            }
    }
}

sealed class NavigationCommand {
    object NavigateUp : NavigationCommand()
    data class NavigateToRoute(val route: String, val options: NavOptions? = null) :
        NavigationCommand()

    data class PopUpToRoute(val route: String, val inclusive: Boolean) : NavigationCommand()
}