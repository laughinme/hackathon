package com.example.hackathon.presentation.screens.onboardingScreens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.PreviewTheme
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent
import kotlin.math.tan

data class GreetingPage(
    val title: String,
    val description: String,
)

val greetingPages = listOf(
    GreetingPage("Стёпа бот", "Он прям ботяра полнейший"),
    GreetingPage("Марк очень крут", "Ну он прям крутейший"),
    GreetingPage("Миша - шиша", "Миша большая шиша")
)

@Composable
fun GreetingScreen() {
    val pagerState = rememberPagerState { greetingPages.size }
    val scope = rememberCoroutineScope()
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { pageIndex ->
                val page = greetingPages[pageIndex]

                //Сама страница заполняемая данными из page
                Box(modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.CenterStart) {
                    Column(
                    ) {
                        Text(
                            text = page.title,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = page.description
                        )
                    }
                }
            }
            //Три точки эти снизу
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(24.dp)
            ) {
                repeat(greetingPages.size) { index ->
                    val width = animateDpAsState(targetValue = if (index == pagerState.currentPage) 25.dp else 10.dp, label = "indicatin_width").value
                    Box(
                        modifier = Modifier
                            .height(10.dp)
                            .width(width)
                            .clip(CircleShape)
                            .background(if (index == pagerState.currentPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
                    )
                }
            }
            //Кнопка для переход дальше
            Row(modifier = Modifier.fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically) {
                if (pagerState.currentPage == greetingPages.size - 1) {
                    TextButton(modifier = Modifier.height(50.dp), onClick = {


                        //TODO  Миша сделай тут переход на LogRegScreen



                    }) {
                        Text("Next")
                    }
                } else {
                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
}


@Preview(showBackground = true, name = "Light Theme")
@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Theme"
)
@Composable
fun OlympiadItemShortPreview() {
    PreviewTheme {
        GreetingScreen()
    }
}




