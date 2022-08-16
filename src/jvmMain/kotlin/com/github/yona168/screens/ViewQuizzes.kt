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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.yona168.Centered
import com.github.yona168.questions.QuizMeta
import com.github.yona168.database.Database
import com.github.yona168.questions.Quiz
import kotlinx.coroutines.launch

@Composable
fun ViewQuizzes(database: Database, openEditTo: (QuizMeta) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val quizzes = remember { mutableStateListOf<QuizMeta>() }
    val scrollState = rememberLazyListState()
    coroutineScope.launch {
        quizzes.addAll(database.listQuizzes())
    }
    Centered {
        LazyColumn(state = scrollState) {
            items(quizzes.size) { index ->
                QuizCard(quizzes[index], openEditTo)
                Spacer(modifier = Modifier.padding(10.dp))
            }
        }
    }
}

@Composable
fun QuizCard(quiz: QuizMeta, openEditTo: (QuizMeta)->Unit){
    Card{
        Row{
            Text("${quiz.name} - ${quiz.author}")
            IconButton(onClick={openEditTo(quiz)}){
                Icon(Icons.Default.Edit, "Edit Quiz")
            }
        }
    }
}