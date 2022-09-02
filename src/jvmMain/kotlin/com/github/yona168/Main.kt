package com.github.yona168// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.yona168.database.Database
import com.github.yona168.database.SimpleFileDatabase
import com.github.yona168.questions.QuizMeta
import com.github.yona168.screens.EditQuiz
import com.github.yona168.screens.Home
import com.github.yona168.screens.playquiz.PlayQuiz
import com.github.yona168.screens.ViewQuizzes
import java.util.*


@Composable
@Preview
fun App() {
    var currentScreen by remember { mutableStateOf(Screen.Home) }
    var quizToShow by remember { mutableStateOf<UUID?>(null) }
    val changeScreen = { screen: Screen -> currentScreen = screen }
    val config: Config = remember { Config() }
    val database: Database = remember { SimpleFileDatabase(config) }
    val openEditTo = { quizMeta: QuizMeta ->
        quizToShow = quizMeta.id
        currentScreen = Screen.EditQuiz
    }
    val openPlayTo = { quizMeta: QuizMeta ->
        quizToShow = quizMeta.id
        currentScreen = Screen.PlayQuiz
    }
    Theme {
        Common(changeScreen = changeScreen) {
            when (currentScreen) {
                Screen.Home -> Home(changeScreen)
                Screen.ViewQuizzes -> ViewQuizzes(database, openEditTo, openPlayTo)
                Screen.CreateQuiz -> EditQuiz(database, { changeScreen(Screen.ViewQuizzes) })
                Screen.EditQuiz -> EditQuiz(database, { changeScreen(Screen.ViewQuizzes) }, quizToShow)
                Screen.PlayQuiz -> PlayQuiz(database, quizToShow as UUID)
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

enum class Screen {
    Home,
    ViewQuizzes,
    CreateQuiz,
    EditQuiz,
    PlayQuiz
}

@Composable
fun Common(changeScreen: (Screen) -> Unit, content: @Composable () -> Unit) {
    Row(modifier = Modifier.fillMaxSize().background(Theme.colors.primary)) {
        Column(modifier = Modifier.background(Theme.colors.secondary).fillMaxHeight().width(50.dp)) {
            IconButton(onClick = { changeScreen(Screen.Home) }) {
                Icon(Icons.Filled.Home, contentDescription = "Home")
            }
        }
        content()
    }
}