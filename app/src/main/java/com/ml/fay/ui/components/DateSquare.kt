package com.ml.fay.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ml.fay.extensions.getDayOfMonth
import com.ml.fay.extensions.getShortMonth
import com.ml.fay.ui.theme.UiConstants
import java.util.Date
import java.util.Locale

@Composable
fun DateSquare(date: Date) {
    Card(shape = RoundedCornerShape(8.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = UiConstants.cardPadding, vertical = 4.dp)
        ) {
            Text(
                date.getShortMonth().orEmpty().uppercase(Locale.ROOT),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                date.getDayOfMonth(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }

}