package com.hicham.wcstoreapp.util

import app.cash.paging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*

class PagingHelper<T : Any>(
    val flow: Flow<PagingData<T>>
) {
    private val IncompleteLoadState = LoadStateNotLoading(false)
    private val InitialLoadStates = LoadStates(
        IncompleteLoadState,
        IncompleteLoadState,
        IncompleteLoadState
    )

    private val _itemSnapshotList = MutableStateFlow(
        ItemSnapshotList<T>(
            0,
            0,
            emptyList()
        )
    )
    val itemSnapshotList = _itemSnapshotList.asStateFlow()

    private val _loadStates = MutableStateFlow(
        CombinedLoadStates(
            refresh = InitialLoadStates.refresh,
            prepend = InitialLoadStates.prepend,
            append = InitialLoadStates.append,
            source = InitialLoadStates
        )
    )
    val loadStates = _loadStates.asStateFlow()

    private val mainDispatcher = Dispatchers.Main

    private val differCallback: DifferCallback = object : DifferCallback {
        override fun onChanged(position: Int, count: Int) {
            if (count > 0) {
                updateItemSnapshotList()
            }
        }

        override fun onInserted(position: Int, count: Int) {
            if (count > 0) {
                updateItemSnapshotList()
            }
        }

        override fun onRemoved(position: Int, count: Int) {
            if (count > 0) {
                updateItemSnapshotList()
            }
        }
    }

    private val pagingDataDiffer = object : PagingDataDiffer<T>(
        differCallback = differCallback,
        mainDispatcher = mainDispatcher
    ) {
        override suspend fun presentNewList(
            previousList: NullPaddedList<T>,
            newList: NullPaddedList<T>,
            lastAccessedIndex: Int,
            onListPresentable: () -> Unit
        ): Int? {
            onListPresentable()
            updateItemSnapshotList()
            return null
        }
    }

    /**
     * Returns the presented item at the specified position, notifying Paging of the item access to
     * trigger T loads necessary to fulfill prefetchDistance.
     *
     * @see peek
     */
    operator fun get(index: Int): T? {
        pagingDataDiffer[index] // this registers the value load
        return itemSnapshotList.value[index]
    }

    /**
     * Returns the presented item at the specified position, without notifying Paging of the item
     * access that would normally trigger page loads.
     *
     * @param index Index of the presented item to return, including placeholders.
     * @return The presented item at position [index], `null` if it is a placeholder
     */
    fun peek(index: Int): T? {
        return itemSnapshotList.value[index]
    }

    fun retry() {
        pagingDataDiffer.retry()
    }

    fun refresh() {
        pagingDataDiffer.refresh()
    }

    private fun updateItemSnapshotList() {
        _itemSnapshotList.value = pagingDataDiffer.snapshot()
    }

    suspend fun collect() = coroutineScope {
        pagingDataDiffer.loadStateFlow.onEach {
            _loadStates.value = it
        }.launchIn(this)

        flow.collectLatest {
            pagingDataDiffer.collectFrom(it)
        }
    }
}
