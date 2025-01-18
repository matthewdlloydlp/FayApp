package com.ml.fay.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ml.fay.R
import com.ml.fay.ui.theme.UiConstants

@Composable
fun ColumnScope.FayHeader() {
    Image(
        painter = painterResource(id = R.drawable.fay_no_background),
        contentDescription = stringResource(R.string.app_logo),
        modifier = Modifier
            .height(80.dp)
            .padding(horizontal = UiConstants.screenPadding, vertical = UiConstants.cardPadding)
    )
    HorizontalDivider(thickness = 1.dp)
}