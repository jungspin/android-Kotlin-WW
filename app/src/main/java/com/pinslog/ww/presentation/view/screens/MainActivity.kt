package com.pinslog.ww.presentation.view.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.pinslog.ww.R
import com.pinslog.ww.model.ForecastDO
import com.pinslog.ww.presentation.model.CurrentWeather
import com.pinslog.ww.presentation.model.HourlyForecast
import com.pinslog.ww.presentation.model.Status
import com.pinslog.ww.presentation.model.UiState
import com.pinslog.ww.presentation.view.components.MinMaxToggleGroup
import com.pinslog.ww.presentation.view.components.WearInfoItem
import com.pinslog.ww.presentation.view.components.WwCard
import com.pinslog.ww.presentation.view.components.WwTextHeadLine
import com.pinslog.ww.presentation.view.components.WwTextLarge
import com.pinslog.ww.presentation.view.components.WwTextMedium
import com.pinslog.ww.presentation.view.components.WwTextSmall
import com.pinslog.ww.presentation.view.screens.ui.theme.Blue1
import com.pinslog.ww.presentation.view.screens.ui.theme.LightTextColor
import com.pinslog.ww.presentation.view.screens.ui.theme.WWTheme
import com.pinslog.ww.presentation.viewmodel.WeatherViewModel
import com.pinslog.ww.util.Utility
import com.pinslog.ww.util.noRippleClickable
import dagger.hilt.android.AndroidEntryPoint

const val LOG_TAG = "ComposeTest"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherViewModel.getCurrentLocation()
        setContent {
            CurrentSurface {
                val forecastState by weatherViewModel.forecastWeather.collectAsState()
                LazyColumn(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    item {
                        val currentWeatherState by weatherViewModel.currentWeatherStateFlow.collectAsState()

                        when (currentWeatherState.status) {
                            Status.LOADING, Status.SUCCESS -> {
                                CurrentWeatherCard(currentWeatherState) {
                                    currentWeatherState.data?.let {
                                        createDynamicLink(it)
                                    }
                                }
                            }

                            Status.FAIL -> {}
                            Status.ERROR -> {}
                        }
                    }

                    when (forecastState.status) {
                        Status.LOADING -> {}
                        Status.SUCCESS -> {
                            forecastState.data?.let { data ->
                                items(data) { forecastDo ->
                                    var isExpand by remember {
                                        mutableStateOf(false)
                                    }
                                    forecastDo?.let {
                                        ForecastItem(item = it, isExpand = isExpand) {
                                            isExpand = !isExpand
                                        }
                                    }
                                }
                            }
                        }

                        Status.FAIL -> {}
                        Status.ERROR -> {}
                    }
                }
            }
        }
    }

    /**
     * 동적 링크를 생성합니다.
     */
    private fun createDynamicLink(currentWeather: CurrentWeather) {
        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse("https://www.pinslog.com/"))
            .setDomainUriPrefix("https://pinslog.page.link")
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
            .setSocialMetaTagParameters(
                DynamicLink.SocialMetaTagParameters.Builder()
                    .setTitle("Wear Weather")
                    .setDescription("기온별 옷차림 안내 어플")
                    .build()
            )
            .buildShortDynamicLink()
            .addOnSuccessListener {
                val shortLink = it.shortLink!!
                createShareSheet(currentWeather, shortLink)
            }
            .addOnFailureListener {
                Toast.makeText(this, "일시적 문제로 데이터를 공유할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * 공유할 내용을 생성합니다.
     *
     * @param dynamicLink 동적 링크
     */
    private fun createShareSheet(currentWeather: CurrentWeather, dynamicLink: Uri) {
        val content = """
            ${currentWeather.currentLocation}의 현재 기온은 ${currentWeather.currentTemp}°.
            ${currentWeather.wearInfo.infoDescription}를 추천드려요.
            자세한 정보가 궁금하다면? $dynamicLink
        """.trimIndent()

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, content)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    companion object {
        val sampleCurrentWeather = CurrentWeather(
            currentLocation = "부산광역시 해운대구 센텀중앙로",
            currentTime = "2024년 7월 19일 목 23시 45분",
            currentTemp = "23",
            weatherDescription = "맑음",
            weatherIcon = R.drawable.ic_clear,
            wearInfo = Utility.getWearingInfo("23".toDouble())
        )
        val sampleForecastList = mutableListOf<ForecastDO>(
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
    }
}

@Composable
fun CurrentSurface(content: @Composable () -> Unit) {
    WWTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            content()
        }
    }
}

/**
 * 현재 날씨 레이아웃
 *
 * @param onShareButtonClick 공유하기 버튼 클릭 시 행위
 */
@Composable
fun CurrentWeatherCard(
    currentWeatherState: UiState<CurrentWeather>,
    onShareButtonClick: () -> Unit
) {
    var isExpand by remember {
        mutableStateOf(false)
    }
    val status = currentWeatherState.status
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_loading_ww)
    )
    val lottieAnimatable = rememberLottieAnimatable()
    LaunchedEffect(composition) {
        lottieAnimatable.animate(
            composition = composition,
            clipSpec = LottieClipSpec.Frame(0, 1200),
            reverseOnRepeat = true,
            initialProgress = 0f
        )
    }
    currentWeatherState.data?.let { currentWeather ->
        WwCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WwTextHeadLine(text = currentWeather.currentLocation)
                WwTextLarge(text = currentWeather.currentTime)
                if (status == Status.LOADING) {
                    LottieAnimation(
                        composition = composition,
                        progress = { lottieAnimatable.progress },
                        modifier = Modifier.height(150.dp)
                    )
                } else if (status == Status.SUCCESS) {
                    Image(
                        painterResource(id = currentWeather.weatherIcon),
                        contentDescription = "날씨 이미지"
                    )
                }

                WwTextMedium(text = "${currentWeather.currentTemp}°")
                WwTextMedium(text = currentWeather.weatherDescription)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CurrentWeatherButton(text = "오늘 뭐입지?", modifier = Modifier) {
                        isExpand = !isExpand
                    }
                    CurrentWeatherButton(text = "공유하기", modifier = Modifier) {
                        onShareButtonClick()
                    }
                }
                if (isExpand) {
                    WearInfoItem(wearInfo = currentWeather.wearInfo, modifier = Modifier)
                }
            }
        }
    }

}

/**
 * 날씨 예보 아이템
 *
 * @param item ForecastDo
 * @param isExpand 예보 펼침 여부
 * @param onClickForecastRow 예보 아이템 클릭 시 행위
 */
@Composable
fun ForecastItem(item: ForecastDO, isExpand: Boolean, onClickForecastRow: () -> Unit) {
    WwCard {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable {
                        onClickForecastRow()
                    },
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                WwTextMedium(text = "${item.month}.${item.date}")
                WwTextMedium(text = item.day)
                Image(
                    painterResource(id = item.resourceId),
                    contentDescription = "날씨 이미지",
                    modifier = Modifier.size(30.dp),
                )
                WwTextMedium(text = "${item.minTemp}°")
                WwTextMedium(text = "${item.maxTemp}°")
                Icon(
                    imageVector = if (isExpand) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "날씨 정보 더보기 아이콘"
                )
            }
            if (isExpand) { // TODO: 애니메이션 추가하자
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
                                WwTextMedium(text = "${item.time}시")
                                Image(
                                    painterResource(id = item.resourceId),
                                    contentDescription = "시간별 날씨 이미지",
                                    modifier = Modifier.size(35.dp),
                                )
                                WwTextMedium(text = "${item.temp}°")
                            }
                        }
                    }
                    var isMaxTemp by remember {
                        mutableStateOf(true)
                    }
                    val wearInfo = if (isMaxTemp) {
                        Utility.getWearingInfo(item.maxTemp.toDouble())
                    } else {
                        Utility.getWearingInfo(item.minTemp.toDouble())
                    }
                    WearInfoItem(wearInfo = wearInfo, modifier = Modifier)

                    // 최대/최소 기온 버튼
                    Row(
                        modifier = Modifier
                            .height(IntrinsicSize.Max)
                            .fillMaxHeight()
                            .padding(0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(0.5f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            WwTextSmall(text = "비올 확률 : ${item.pop}%", textColor = LightTextColor)
                        }

                        MinMaxToggleGroup(
                            items = listOf("max", "min"),
                            modifier = Modifier.weight(0.5f)
                        ) { selectedButtonText ->
                            // max/min 컴포넌트를 하나씩 만들어두고, 버튼 클릭에 따라 어떤걸 보여줄지 결정
                            isMaxTemp = selectedButtonText == "max"
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun CurrentWeatherButton(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = {
            onClick()
        },
        colors = ButtonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Blue,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent
        ),
        modifier = modifier,
    ) {
        WwTextMedium(text = text, textColor = Blue1)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val sampleUiState = UiState<CurrentWeather>(
        status = Status.LOADING,
        message = "",
        data = MainActivity.sampleCurrentWeather
    )
    CurrentSurface {
        LazyColumn(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                CurrentWeatherCard(sampleUiState) {

                }
            }
            items(MainActivity.sampleForecastList) {
                var isExpand by remember {
                    mutableStateOf(false)
                }
                ForecastItem(item = it, isExpand = isExpand) {
                    isExpand = !isExpand
                }
            }
        }
    }
}