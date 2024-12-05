package com.example.nok.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nok.utils.PreferenceUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPage(
    navController: NavController,
    context: Context,
    viewModel: UserPageViewModel = viewModel(),
    email: String
) {
    val uiState = viewModel.state.collectAsState() // ViewModel 상태를 구독
    val username = email.substringBefore("@")
    var isExample by remember { mutableStateOf(PreferenceUtils.loadState(context)) }
    var lastRefreshTime by remember { mutableStateOf(PreferenceUtils.loadString(context, "lastRefreshTime")) }
    var userScore by remember { mutableStateOf(0) } // 점수를 저장하는 상태 추가

    LaunchedEffect(key1 = uiState.value) {
        if (uiState.value == SignOutState.LoggedOut)
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(username) },
                actions = {
                    IconButton(onClick = {
                        RefreshData(
                            username = username,
                            onExampleChange = { newState ->
                                isExample = newState
                                PreferenceUtils.saveState(context, newState)
                            },
                            onDateTimeChange = { dateTime ->
                                lastRefreshTime = dateTime
                                PreferenceUtils.saveString(context, "lastRefreshTime", dateTime)
                            },
                            onScoreChange = { score -> // 점수 업데이트 콜백 추가
                                userScore = score
                            }
                        )
                    }) {
                        Icon(imageVector = Icons.Filled.Refresh, contentDescription = "refresh")
                    }
                    IconButton(onClick = { viewModel.signOut() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF1F1F1))
                .padding(16.dp)
        ) {
            Text(
                text = "마지막 업데이트: $lastRefreshTime",
                color = Color(0xFF6D6D6D),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.End)
            )
            Spacer(modifier = Modifier.height(6.dp))
            if (isExample) {
                StatGreen()
            } else {
                StatRed()
            }
            Spacer(modifier = Modifier.height(16.dp))

            // RefreshData에서 받아온 점수를 표시하는 Text
            Text(
                text = "현재 점수: $userScore",
                color = Color.Black,
                fontSize = 60.sp,
                //fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}


@Composable
fun StatGreen() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF77EF68)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(250.dp),
                shape = CircleShape,
                color = Color.White // 흰색 원 배경
            ) {
                Box(
                    contentAlignment = Alignment.Center, // 텍스트를 가운데 정렬
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "\u2714",
                        color = Color(0xFF77EF68),
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "치매 안전군",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "치매가 의심되지 않습니다",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun StatRed() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFF6C6C)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(250.dp),
                shape = CircleShape,
                color = Color.White // 흰색 원 배경
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "!",
                        color = Color(0xFFFF6C6C),
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "치매 의심군",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "치매가 의심됩니다. \n전문의 상담이 필요합니다.",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun EmotionStats(stat1: Int, stat2: Int, stat3: Int, stat4: Int, stat5: Int, stat6: Int, stat7: Int, stat8: Int) {
    val stats = listOf(stat1, stat2, stat3, stat4, stat5, stat6, stat7, stat8)
    val colors = listOf(
        Color.Gray, Color.Yellow, Color.Blue, Color.Magenta,
        Color.Red, Color.Green, Color.Cyan, Color(0xFFFFA500) // 주황색
    )
    val labels = listOf("중립", "기쁨", "슬픔", "코믹", "공포", "편안함", "아름다움", "신나는")

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp), // 둥근 모서리
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 첫 번째 Row (상위 4개 상태)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                stats.zip(colors).zip(labels).take(4).forEach { (statColorPair, label) ->
                    val (stat, color) = statColorPair
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        CircularStatChart(stat = stat, color = color) // 원의 색상 적용
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "$label")
                    }
                }
            }

            // 두 번째 Row (하위 4개 상태)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                stats.zip(colors).zip(labels).drop(4).forEach { (statColorPair, label) ->
                    val (stat, color) = statColorPair
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        CircularStatChart(stat = stat, color = color) // 원의 색상 적용
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "$label")
                    }
                }
            }
        }
    }
}

@Composable
fun CircularStatChart(stat: Int, color: Color) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(60.dp)
    ) {
        Canvas(modifier = Modifier.size(60.dp)) {
            // 배경 원 그리기 (회색 예시)
            drawArc(
                color = Color(0xFFF1F1F1), // 배경 원 색상 지정
                startAngle = 0f,
                sweepAngle = 360f, // 전체 원을 그림
                useCenter = true
            )

            // 퍼센트에 따른 원 그리기
            drawArc(
                color = color, // 퍼센트 원 색상 지정
                startAngle = -90f,
                sweepAngle = 3.6f * stat, // 퍼센트에 따라 각도 설정
                useCenter = true
            )
        }

        // 퍼센트 텍스트
        Text(
            text = "$stat%",
            color = Color.Black
        )
    }
}

@Composable
fun EmotionSummary(positive: Int, negative: Int) {
    val total = positive + negative

    val positivePercent = if (total > 0) (positive.toFloat() / total) * 100 else 0f
    val negativePercent = if (total > 0) (negative.toFloat() / total) * 100 else 0f

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 제목
            Text(
                text = "컨디션",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 게이지 바
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFF1F1F1))
            ) {
                // 긍정적 게이지 바
                Box(
                    modifier = Modifier
                        .weight(positive.toFloat())
                        .fillMaxHeight()
                        .background(Color(0xFF77EF68))
                )
                // 부정적 게이지 바
                Box(
                    modifier = Modifier
                        .weight(negative.toFloat())
                        .fillMaxHeight()
                        .background(Color(0xFFFF6C6C))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 백분율과 레이블 표시
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 긍정적 레이블과 퍼센트
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "긍정적",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Text(
                        text = "${positivePercent.toInt()}%",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                // 부정적 레이블과 퍼센트
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "부정적",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Text(
                        text = "${negativePercent.toInt()}%",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}


fun RefreshData(
    username: String,
    onExampleChange: (Boolean) -> Unit,
    onDateTimeChange: (String) -> Unit,
    onScoreChange: (Int) -> Unit // 점수를 업데이트하는 콜백 추가
) {
    RetrofitClient.apiService.getUserData(username).enqueue(object : Callback<UserDataClass> {
        override fun onResponse(call: Call<UserDataClass>, response: Response<UserDataClass>) {
            if (response.isSuccessful) {
                val userData = response.body()
                if (userData != null) {
                    Log.d("Refresh", "데이터 수신 성공: $userData")

                    val newState = userData.data.total_score > 71
                    onExampleChange(newState)

                    val currentDateTime = getCurrentDateTime()
                    onDateTimeChange(currentDateTime)

                    onScoreChange(userData.data.total_score) // 점수 업데이트 콜백 호출
                } else {
                    Log.e("Refresh", "응답 데이터가 null입니다.")
                }
            } else {
                Log.e("Refresh", "오류 발생: ${response.code()} - ${response.message()}")
            }
        }

        override fun onFailure(call: Call<UserDataClass>, t: Throwable) {
            Log.e("Refresh", "네트워크 오류: ${t.message}")
        }
    })
}
fun getCurrentDateTime(): String {
    val dateFormat = SimpleDateFormat("yyyy. MM. dd. HH:mm", Locale.getDefault())
    return dateFormat.format(Date())
}

