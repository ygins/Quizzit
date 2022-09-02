package com.github.yona168.screens

import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.github.yona168.Centered
import com.github.yona168.Screen
import org.jetbrains.skia.FontStyle


@Composable
fun Home(changeScreen: (Screen) -> Unit) {
    Centered {
        Text("Quizzit", fontWeight = FontWeight.ExtraBold, fontSize = 30.sp)
        OutlinedButton(onClick = { changeScreen(Screen.ViewQuizzes) }) {
            Text("View Quizzes")
        }
        OutlinedButton(onClick = { changeScreen(Screen.CreateQuiz) }) {
            Text("Create New Quiz")
        }
    }

}

