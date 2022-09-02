package com.github.yona168.screens

import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.github.yona168.Centered
import com.github.yona168.database.Database
import com.github.yona168.questions.QuizMeta
import com.github.yona168.screens.playquiz.PlayQuiz


@Composable
fun Home(database: Database, changeScreen: (@Composable () -> Unit) -> Unit) {
    Centered {
        Text("Quizzit", fontWeight = FontWeight.ExtraBold, fontSize = 30.sp)
        val goHome = { changeScreen { Home(database, changeScreen) } }
        val openEditTo = { quizMeta: QuizMeta ->
            changeScreen {
                EditQuiz(database, goHome = goHome, quizToLoad = quizMeta.id)
            }
        }
        val openPlayTo = { quizMeta: QuizMeta ->
            changeScreen {
                PlayQuiz(database, quizMeta.id)
            }
        }
        OutlinedButton(onClick = {
            changeScreen {
                ViewQuizzes(
                    database,
                    openEditTo = openEditTo,
                    openPlayTo = openPlayTo
                )
            }
        }) {
            Text("View Quizzes")
        }
        OutlinedButton(onClick = { changeScreen { EditQuiz(database, goHome = goHome) } }) {
            Text("Create New Quiz")
        }
    }

}

