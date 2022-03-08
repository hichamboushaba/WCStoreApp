package com.hicham.wcstoreapp.android.ui.common.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable

@Composable
fun ToolbarScreen(
    title: @Composable () -> Unit,
    onNavigationClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            WCTopAppBar(
                title = title,
                onNavigationClick = onNavigationClick
            )
        },
        content = content
    )
}