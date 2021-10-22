package com.hicham.wcstoreapp.ui.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CategoryChip(text: String, isSelected: Boolean, onClicked: () -> Unit) {
    Text(
        text = text,
        style = MaterialTheme.typography.body2,
        color = when {
            isSelected -> MaterialTheme.colors.onBackground
            else -> MaterialTheme.colors.onBackground.copy(alpha = 0.4f)
        },
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clickable(enabled = !isSelected, onClick = onClicked)
            .padding(8.dp)
    )
}

@Preview
@Composable
fun SelectedChipPreview() {
    CategoryChip(text = "Chip", isSelected = true, onClicked = {})
}

@Preview
@Composable
fun UnSelectedChipPreview() {
    CategoryChip(text = "Chip", isSelected = false, onClicked = {})
}