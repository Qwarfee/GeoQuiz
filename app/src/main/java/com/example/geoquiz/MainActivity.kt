package com.example.geoquiz

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val Purple = Color(0xFF6200EE)
val PurpleDark = Color(0xFF3700B3)

// Вопрос теста: текст и правильный ответ
data class Question(val text: String, val answer: Boolean)

val questions = listOf(
    Question("Canberra is the capital of Australia.", true),
    Question("The Pacific Ocean is larger than the Atlantic Ocean.", true),
    Question("The Suez Canal connects the Red Sea and the Indian Ocean.", false),
    Question("The source of the Nile River is in Egypt.", false),
    Question("The Amazon River is the longest river in the Americas.", true),
    Question("Lake Baikal is the world's oldest and deepest freshwater lake.", true)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // светлые значки в строке состояния на фиолетовом фоне
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT))
        setContent {
            MaterialTheme {
                GeoQuizScreen()
            }
        }
    }
}

// Фиолетовая кнопка, как на рисунке.
// visible = false -> кнопка невидима и заблокирована (место под неё сохраняется)
@Composable
fun QuizButton(
    text: String,
    visible: Boolean = true,
    showArrow: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = visible,
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Purple,
            contentColor = Color.White
        ),
        modifier = Modifier.alpha(if (visible) 1f else 0f)
    ) {
        Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Medium, letterSpacing = 1.sp)
        if (showArrow) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "❯", color = Color.White.copy(alpha = 0.6f))
        }
    }
}

@Composable
fun GeoQuizScreen() {
    val context = LocalContext.current

    var currentIndex by rememberSaveable { mutableIntStateOf(0) }  // номер текущего вопроса

    fun checkAnswer(userAnswer: Boolean) {
        val isCorrect = userAnswer == questions[currentIndex].answer
        val message = if (isCorrect) "Correct!" else "Incorrect!"
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // фон строки состояния
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .background(PurpleDark)
        )
        // заголовок GeoQuiz
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Purple)
                .padding(horizontal = 16.dp)
        ) {
            Text(text = "GeoQuiz", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Medium)
        }

        // текст вопроса
        Text(
            text = questions[currentIndex].text,
            color = Color(0xFF757575),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        )

        // кнопки TRUE и FALSE
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            QuizButton(text = "TRUE") { checkAnswer(true) }
            QuizButton(text = "FALSE") { checkAnswer(false) }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // кнопка NEXT: следующий вопрос
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            QuizButton(text = "NEXT", showArrow = true) {
                currentIndex = (currentIndex + 1) % questions.size
            }
        }
    }
}