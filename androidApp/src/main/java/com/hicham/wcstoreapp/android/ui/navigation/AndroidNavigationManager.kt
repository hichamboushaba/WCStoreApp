package com.hicham.wcstoreapp.android.ui.navigation

import androidx.lifecycle.asFlow
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import com.hicham.wcstoreapp.ui.NavigationManager
import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

class AndroidNavigationManager : NavigationManager {
    // We use a StateFlow here to allow ViewModels to start observing navigation results before the initial composition,
    // and still get the navigation result later
    private val navControllerFlow = MutableStateFlow<NavController?>(null)

    private val navigationCommands =
        MutableSharedFlow<NavigationCommand>(extraBufferCapacity = Int.MAX_VALUE)

    private val navigationResults = Channel<NavigationResult>(capacity = Channel.BUFFERED)

    suspend fun handleNavigationCommands(navController: NavController) {
        navigationCommands
            .onSubscription {
                this@AndroidNavigationManager.navControllerFlow.value = navController
            }
            .onCompletion { this@AndroidNavigationManager.navControllerFlow.value = null }
            .collect { navController.handleNavigationCommand(it) }
    }

    fun navigate(route: String, optionsBuilder: (NavOptionsBuilder.() -> Unit)? = null) {
        val options = optionsBuilder?.let { navOptions(it) }
        navigationCommands.tryEmit(NavigationCommand.NavigateToRoute(route, options))
    }

    override fun navigate(route: String) {
        navigate(route, null)
    }

    override fun navigateUp() {
        navigationCommands.tryEmit(NavigationCommand.NavigateUp)
    }

    override fun popUpTo(route: String, inclusive: Boolean) {
        navigationCommands.tryEmit(NavigationCommand.PopUpToRoute(route, inclusive))
    }

    override fun <T: Any> navigateBackWithResult(
        key: String,
        result: T,
        destination: String?
    ) {
        navigationCommands.tryEmit(
            NavigationCommand.NavigateUpWithResult(
                key = key,
                result = result,
                destination = destination
            )
        )
    }

    override fun <T: Any> observeResult(
        key: String,
        route: String?,
        onEach: (T) -> Unit
    ): Closeable {
        val scope = CoroutineScope(Dispatchers.Main)
        navigationResults.receiveAsFlow()
            .filter { it.key == key }
            .onEach {
                (it.value as? T)?.let(onEach)
            }
            .launchIn(scope)

        return Closeable { scope.cancel() }
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
                navigationResults.trySend(
                    NavigationResult(
                        navigationCommand.key,
                        navigationCommand.result
                    )
                )

                navigationCommand.destination?.let {
                    popBackStack(it, false)
                } ?: run {
                    navigateUp()
                }
            }
        }
    }

    private data class NavigationResult(val key: String, val value: Any)
}

sealed class NavigationCommand {
    object NavigateUp : NavigationCommand()
    data class NavigateToRoute(val route: String, val options: NavOptions? = null) :
        NavigationCommand()

    data class NavigateUpWithResult<T: Any>(
        val key: String,
        val result: T,
        val destination: String? = null
    ) : NavigationCommand()

    data class PopUpToRoute(val route: String, val inclusive: Boolean) : NavigationCommand()
}