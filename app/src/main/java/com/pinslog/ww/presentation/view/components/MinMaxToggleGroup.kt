package com.pinslog.ww.presentation.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pinslog.ww.presentation.view.screens.ui.theme.Blue1

/**
 * 최고/최저 기온 옷차림 확인을 위한 토글 버튼
 *
 * @param items 버튼 텍스트 리스트
 * @param modifier
 * @param onItemSelected 버튼이 클릭 되었을 때의 행위
 */
@Composable
fun MinMaxToggleGroup(
    items: List<String>,
    modifier: Modifier,
    onItemSelected: (String) -> Unit,
) {
    var selectedItem by remember {
        mutableStateOf(items.first())
    }

    // 토글 버튼 바깥 레이아웃
    Row(
        modifier = modifier
            .background(color = Color.Transparent)
            .border(BorderStroke(1.dp, color = Color.LightGray), shape = RoundedCornerShape(8.dp)),
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = item == selectedItem
            val backgroundColor =
                if (isSelected) Blue1 else Color.Transparent
            val contentColor = if (isSelected) Color.White else MaterialTheme.colors.onSurface

            val toggleRoundCornerShape: RoundedCornerShape
            if (index == 0) {
                toggleRoundCornerShape = RoundedCornerShape(
                    topStart = 8.dp,
                    bottomStart = 8.dp
                )
            } else {
                toggleRoundCornerShape = RoundedCornerShape(
                    topEnd = 8.dp,
                    bottomEnd = 8.dp
                )
                VerticalDivider(
                    color = Color.LightGray,
                    thickness = 1.dp
                )
            }

            // 토글 버튼
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(backgroundColor, shape = toggleRoundCornerShape)
                    .clickable {
                        selectedItem = item
                        onItemSelected(item)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item,
                    color = contentColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyToggleButtonPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .weight(0.1f)
                .fillMaxWidth()
        ) {
            MinMaxToggleGroup(items = listOf("max", "min"), Modifier) { selectedButtonText ->

            }
        }

    }

}