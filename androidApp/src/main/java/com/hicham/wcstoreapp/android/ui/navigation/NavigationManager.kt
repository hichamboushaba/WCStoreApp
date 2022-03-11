package com.hicham.wcstoreapp.android.ui.navigation

import androidx.lifecycle.asFlow
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationManager @Inject constructor() {
    // We use a StateFlow here to allow ViewModels to start observing navigation results before the initial composition,
    // and still get the navigation result later
    private val navControllerFlow = MutableStateFlow<NavController?>(null)

    private val navigationCommands =
        MutableSharedFlow<NavigationCommand>(extraBufferCapacity = Int.MAX_VALUE)


    suspend fun handleNavigationCommands(navController: NavController) {
        navigationCommands
            .onSubscription { this@NavigationManager.navControllerFlow.value = navController }
            .onCompletion { this@NavigationManager.navControllerFlow.value = null }
            .collect { navController.handleNavigationCommand(it) }
    }

    fun navigate(route: String, optionsBuilder: (NavOptionsBuilder.() -> Unit)? = null) {
        val options = optionsBuilder?.let { navOptions(it) }
        navigationCommands.tryEmit(NavigationCommand.NavigateToRoute(route, options))
    }

    fun navigateUp() {
        navigationCommands.tryEmit(NavigationCommand.NavigateUp)
    }

    fun popUpTo(route: String, inclusive: Boolean = false) {
        navigationCommands.tryEmit(NavigationCommand.PopUpToRoute(route, inclusive))
    }

    fun <T> navigateBackWithResult(
        key: String,
        result: T,
        destination: String? = null
    ) {
        navigationCommands.tryEmit(
            NavigationCommand.NavigateUpWithResult(
                key = key,
                result = result,
                destination = destination
            )
        )
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

    private fun NavController.handleNavigationCommand(navigationCommand: NavigationCommand) {
        when (navigationCommand) {
            is NavigationCommand.NavigateToRoute -> navigate(
                navigationCommand.route,
                navigationCommand.options
            )
            NavigationCommand.NavigateUp -> navigateUp()
            is NavigationCommand.PopUpToRoute -> popBackStack(
                navigationCommand.route,
                navigationCommand.inclusive
            )
            is NavigationCommand.NavigateUpWithResult<*> -> {
                val backStackEntry =
                    navigationCommand.destination?.let { getBackStackEntry(it) }
                        ?: previousBackStackEntry
                backStackEntry?.savedStateHandle?.set(
                    navigationCommand.key,
                    navigationCommand.result
                )

                navigationCommand.destination?.let {
                    popBackStack(it, false)
                } ?: run {
                    navigateUp()
                }
            }
        }
    }
}

sealed class NavigationCommand {
    object NavigateUp : NavigationCommand()
    data class NavigateToRoute(val route: String, val options: NavOptions? = null) :
        NavigationCommand()

    data class NavigateUpWithResult<T>(
        val key: String,
        val result: T,
        val destination: String? = null
    ) : NavigationCommand()

    data class PopUpToRoute(val route: String, val inclusive: Boolean) : NavigationCommand()
}