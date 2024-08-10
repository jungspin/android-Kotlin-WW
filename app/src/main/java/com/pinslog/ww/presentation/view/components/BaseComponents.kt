package com.pinslog.ww.presentation.view.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.pinslog.ww.presentation.view.screens.ui.theme.DefaultTextColor

@Composable
fun WwTextMedium(text: String, textColor: Color = DefaultTextColor) {
    Text(text = text, color = textColor, style = MaterialTheme.typography.bodyMedium)
}

@Composable
fun WwTextLarge(text: String, textColor: Color = DefaultTextColor) {
    Text(
        text = text,
        color = textColor,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun WwTextHeadLine(text: String, textColor: Color = DefaultTextColor) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium,
        color = textColor,
        fontWeight = FontWeight.Bold
    )
}