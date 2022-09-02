package com.github.yona168.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.yona168.Centered
import com.github.yona168.questions.QuizMeta
import com.github.yona168.database.Database
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun ViewQuizzes(database: Database, openEditTo: (QuizMeta) -> Unit, openPlayTo: (QuizMeta) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val quizzes = remember { mutableStateListOf<QuizMeta>() }
    val scrollState = rememberLazyListState()
    coroutineScope.launch {
        quizzes.addAll(database.listQuizzes())
    }
    Centered {
        LazyColumn(state = scrollState) {
            fun deleteQuiz(index: Int): (QuizMeta) -> Unit = {
                runBlocking {
                    database.delete(quizzes[index].id)
                    quizzes.remove(quizzes[index])
                }
            }
            items(quizzes.size) { index ->
                QuizCard(quizzes[index], openEditTo, openPlayTo, deleteQuiz(index))
                Spacer(modifier = Modifier.padding(10.dp))
            }
        }
    }
}

@Composable
fun QuizCard(quiz: QuizMeta, openEditTo: (QuizMeta)->Unit, openPlayTo: (QuizMeta)->Unit, delete: (QuizMeta)->Unit){
    Card{
        Row{
            Text("${quiz.name} - ${quiz.author}")
            IconButton(onClick={openEditTo(quiz)}){
                Icon(Icons.Default.Edit, "Edit Quiz")
            }
            IconButton(onClick={openPlayTo(quiz)}){
                Icon(Icons.Default.PlayArrow, "Play Quiz")
            }
            IconButton(onClick={delete(quiz)}){
                Icon(Icons.Default.Delete, "Delete Quiz")
            }
        }
    }
}