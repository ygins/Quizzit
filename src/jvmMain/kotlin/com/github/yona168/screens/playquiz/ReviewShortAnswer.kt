package com.github.yona168.screens.playquiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.yona168.Centered
import com.github.yona168.questions.Quiz
import com.github.yona168.questions.ShortAnswer

/**
 * Handles reviewing the short answer questions
 */
@Composable
fun ReviewShortAnswer(quiz: Quiz, answers: List<Any?>, correctIndices: MutableSet<Int>, moveOnToReport: () -> Unit) {
    val shortAnswerIndices = remember { quiz.questions.indices.filter { quiz.questions[it] is ShortAnswer } }
    val removed = remember { mutableStateListOf<Int>() }
    Centered {
        LazyColumn {
            for(i in shortAnswerIndices) {
                item{
                    if (i !in removed) {
                        val question = quiz.questions[i] as ShortAnswer
                        Column {
                            Text(
                                "Question ${i + 1}: ${question.question}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text("Your answer:", fontWeight = FontWeight.Bold)
                            Text(answers[i] as String)
                            Text("Correct answer:", fontWeight = FontWeight.Bold)
                            Text(question.answer)
                            Text("Did you get it right?", fontWeight = FontWeight.Bold)
                            CheckShortAnswerButtons(
                                confirmCorrect = { correctIndices += i },
                                remove = { removed += i },
                                checkIfDone = { if (removed.size == shortAnswerIndices.size) moveOnToReport() }
                            )
                        }
                        Spacer(modifier = Modifier.padding(3.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CheckShortAnswerButtons(confirmCorrect: () -> Unit, remove: () -> Unit, checkIfDone: () -> Unit) {
    Row {
        OutlinedButton(onClick = {
            confirmCorrect()
            remove()
            checkIfDone()
        }) {
            Text("Yes!")
        }
        Spacer(modifier = Modifier.padding(3.dp))
        OutlinedButton(onClick = {
            remove()
            checkIfDone()
        }) {
            Text("Ill get it next time")
        }
    }
}
