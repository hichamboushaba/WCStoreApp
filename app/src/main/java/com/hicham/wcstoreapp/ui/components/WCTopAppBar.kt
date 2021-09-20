package com.hicham.wcstoreapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WCTopAppBar(
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "",
            tint = MaterialTheme.colors.primary
        )
    },
    onNavigationClick: () -> Unit,
) {
    InsetAwareTopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
                    .padding(end = 48.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                title()
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                navigationIcon()
            }
        },
        backgroundColor = MaterialTheme.colors.surface
    )
}
