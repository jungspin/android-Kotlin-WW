package com.pinslog.ww.presentation.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pinslog.ww.presentation.view.screens.ui.theme.WWTheme


@Composable
fun MultiToggleButton(
    currentSelection: String,
    toggleStates: List<String>,
    onToggleChange: (String) -> Unit
) {
    val selectedTint = MaterialTheme.colors.primary
    val unselectedTint = Color.Unspecified

    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .border(BorderStroke(1.dp, Color.LightGray))
    ) {
        toggleStates.forEachIndexed { index, toggleState ->
            val isSelected = currentSelection == toggleState
            val backgroundTint = if (isSelected) selectedTint else unselectedTint
            val textColor = if (isSelected) Color.White else Color.Unspecified



            Row(
                modifier = Modifier
                    .background(backgroundTint)
                    .padding(vertical = 6.dp, horizontal = 8.dp)
                    .toggleable(
                        value = isSelected,
                        enabled = true,
                        onValueChange = { selected ->
                            if (selected) {
                                onToggleChange(toggleState)
                            }
                        })
            ) {
                Text(toggleState, color = textColor, modifier = Modifier.padding(4.dp))
            }

            if (index != 0) {
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
            }

        }
    }
}

@Composable
fun MinMaxToggleGroup(
    items: List<String>,
    onItemSelected: (String) -> Unit,
) {
    var selectedItem by remember {
        mutableStateOf(items.first())
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(16.dp)
            .background(color = Color.Transparent)
            .border(BorderStroke(1.dp, color = Color.LightGray), shape = RoundedCornerShape(8.dp)),
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = item == selectedItem
            val backgroundColor =
                if (isSelected) MaterialTheme.colors.primary else Color.Transparent
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
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
            }



            Row(
                modifier = Modifier
                    .background(backgroundColor, shape = toggleRoundCornerShape)
                    .padding(vertical = 6.dp, horizontal = 8.dp)
                    .clickable {
                        selectedItem = item
                        onItemSelected(item)
                    }
            ) {
                Text(text = item, color = contentColor)
            }

//            Button(
//                onClick = {
//                    selectedItem = item
//                    onItemSelected(item)
//                },
//                colors = ButtonDefaults.buttonColors(
//                    backgroundColor = backgroundColor,
//                    contentColor = contentColor
//                ),
//                shape = RoundedCornerShape(8.dp)
//            ) {
//                Text(text = item)
//            }
        }
    }
}