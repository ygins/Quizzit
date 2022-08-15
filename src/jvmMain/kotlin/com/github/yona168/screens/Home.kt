package com.github.yona168.screens

import Screen
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.github.yona168.Centered


@Composable
fun Home(changeScreen: (Screen) -> Unit) {
    Centered {
        OutlinedButton(onClick = { changeScreen(Screen.ViewQuizzes) }) {
            Text("View Quizzes")
        }
        OutlinedButton(onClick = {changeScreen(Screen.EditQuiz)}){
            Text("Create New Quiz")
        }
    }

}

