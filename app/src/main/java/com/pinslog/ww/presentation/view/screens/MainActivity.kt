package com.pinslog.ww.presentation.view.screens

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pinslog.ww.R
import com.pinslog.ww.model.ForecastDO
import com.pinslog.ww.presentation.model.HourlyForecast
import com.pinslog.ww.presentation.view.components.MultiToggleButton
import com.pinslog.ww.presentation.view.screens.ui.theme.WWTheme
import com.pinslog.ww.util.Utility

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isExpandForecast by remember {
                mutableStateOf(false)
            }

            WWTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CurrentWeatherLayout("Android")
                }
            }
        }
    }
}

@Composable
fun CurrentWeatherLayout(name: String, modifier: Modifier = Modifier) {

    LazyColumn(modifier = Modifier.padding(18.dp)) {
        item {
            OutlinedCard(modifier = Modifier.clickable { }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "부산광역시 해운대구 재반로",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "2024년 7월 18일 목 23:39",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Image(
                        painterResource(id = R.drawable.ic_clear),
                        contentDescription = "날씨 이미지"
                    )
                    Text(text = "23°", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "맑음", style = MaterialTheme.typography.bodyMedium)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {},
                            colors = ButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Blue,
                                disabledContainerColor = Color.Transparent,
                                disabledContentColor = Color.Transparent
                            )
                        ) {
                            Text(text = "오늘 뭐입지?", style = MaterialTheme.typography.bodyMedium)
                        }
                        Button(
                            onClick = {},
                            colors = ButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Blue,
                                disabledContainerColor = Color.Transparent,
                                disabledContentColor = Color.Transparent
                            )
                        ) {
                            Text(text = "공유하기", style = MaterialTheme.typography.bodyMedium)
                        }

                    }
                }
            }
            // end currentWeather
        }
        // start list
        val sampleList = mutableListOf<ForecastDO>(
            ForecastDO(
                month = "07",
                date = "23",
                id = 2131165344,
                pop = 16,
                maxTemp = "24",
                minTemp = "24",
                hourlyMap = mutableMapOf<String, HourlyForecast>(
                    "21" to HourlyForecast(
                        time = 21,
                        resourceId = 2131165344,
                        temp = 24
                    )
                )
            ),
            ForecastDO(
                month = "07",
                date = "24",
                id = R.drawable.ic_clear,
                pop = 0,
                maxTemp = "28",
                minTemp = "24",
                hourlyMap = mutableMapOf<String, HourlyForecast>(
                    "0" to HourlyForecast(
                        time = 0,
                        resourceId = R.drawable.ic_cloudy_2,
                        temp = 27
                    ),
                    "3" to HourlyForecast(3, R.drawable.ic_dusty, 28),
                    "6" to HourlyForecast(3, R.drawable.ic_dusty, 28),
                    "9" to HourlyForecast(3, R.drawable.ic_dusty, 28),
                    "12" to HourlyForecast(3, R.drawable.ic_dusty, 28),
                    "15" to HourlyForecast(3, R.drawable.ic_dusty, 28),
                    "18" to HourlyForecast(3, R.drawable.ic_dusty, 28),
                    "21" to HourlyForecast(3, R.drawable.ic_dusty, 28),
                )
            )
        )


        items(sampleList) { item ->
            OutlinedCard {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(text = "${item.month}.${item.date}")
                        Text(text = item.day)
                        Image(
                            painterResource(id = R.drawable.ic_clear),
                            contentDescription = "날씨 이미지",
                            modifier = Modifier.size(30.dp),
                        )
                        Text(text = "${item.minTemp}°")
                        Text(text = "${item.maxTemp}°")
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "날씨 정보 더보기 아이콘"
                        )
                    }
                    // Expand
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        LazyRow {
                            // 시간별 예보
                            items(items = item.hourlyMap.values.toList()) { item: HourlyForecast ->
                                Column(
                                    Modifier.padding(
                                        vertical = 12.dp,
                                        horizontal = 12.dp
                                    ),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "${item.time}시")
                                    Image(
                                        painterResource(id = item.resourceId),
                                        contentDescription = "시간별 날씨 이미지",
                                        modifier = Modifier.size(35.dp),
                                    )
                                    Text(text = "${item.temp}°")
                                }
                            }
                        }
                        val wearInfo =
                            Utility.getWearingInfo(item.maxTemp.toDouble())
                        // 의복 이미지
                        Row(
                            modifier = Modifier.fillMaxWidth(),
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
                        // 최대/최소 기온 버튼
                        MultiToggleButton(currentSelection = "max", toggleStates = listOf("max", "min")) {
                            Log.d("toggleTest", "CurrentWeatherLayout: $it")
                        }
//                        OutlinedIconToggleButton(checked = true, onCheckedChange = ) {
//
//                        }
                    }
                }
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WWTheme {
        CurrentWeatherLayout("Android")
    }
}