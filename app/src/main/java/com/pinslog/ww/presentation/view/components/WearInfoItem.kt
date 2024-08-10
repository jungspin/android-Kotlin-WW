package com.pinslog.ww.presentation.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pinslog.ww.model.WearInfo
import com.pinslog.ww.util.Utility

/**
 * 옷차림 설명 레이아웃
 *
 * @param wearInfo
 * @param modifier
 */
@Composable
fun WearInfoItem(
    wearInfo: WearInfo,
    modifier: Modifier,
) {
    // 의복 이미지
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (resourceId in wearInfo.wearingList) {
            Image(
                painterResource(id = resourceId),
                contentDescription = "온도별 의상 이미지",
                modifier = Modifier
                    .size(80.dp),
            )
        }
    }
    // 옷차림 설명
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        textAlign = TextAlign.Center,
        text = wearInfo.infoDescription
    )
}

@Composable
@Preview(showBackground = true)
fun WearInfoItemPreview() {
    WearInfoItem(wearInfo = Utility.getWearingInfo("23".toDouble()), modifier = Modifier)
}