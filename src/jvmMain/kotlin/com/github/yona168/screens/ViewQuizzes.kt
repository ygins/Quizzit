package com.github.yona168.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.Text
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

@Composable
fun ViewQuizzes(database: Database) {
    val coroutineScope = rememberCoroutineScope()
    val quizzes = remember { mutableStateListOf<QuizMeta>() }
    val scrollState = rememberLazyListState()
    coroutineScope.launch {
        quizzes.addAll(database.listQuizzes())
    }
    Centered {
        LazyColumn(state = scrollState) {
            items(quizzes.size) { index ->

                Card {
                    Text("${quizzes[index].name} - ${quizzes[index].author}")
                }

                Spacer(modifier = Modifier.padding(10.dp))
            }
        }
    }

}