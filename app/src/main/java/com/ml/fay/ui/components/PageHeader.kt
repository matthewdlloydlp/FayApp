package com.ml.fay.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ml.fay.ui.theme.UiConstants

@Composable
fun ColumnScope.PageHeader(title: String, showDivider: Boolean = false) {
    Text(
        title,
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(UiConstants.screenPadding)
    )
    if (showDivider) {
        HorizontalDivider(thickness = 1.dp)
    }
}