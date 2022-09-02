package com.github.yona168.screens.playquiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.yona168.Centered
import com.github.yona168.questions.ManyChoice
import com.github.yona168.questions.OptionsQuestion
import com.github.yona168.questions.Quiz
import com.github.yona168.questions.ShortAnswer

@Composable
fun ReportOptionsCard(question: OptionsQuestion, show: Set<Int>) {
    for (i in question.options.indices) {
        Row {
            RadioButton(selected = show.contains(i), onClick = {})
            Text("\n${question.options[i]}")
        }
    }
}

@Composable
fun FinalReport(quiz: Quiz, answers: List<Any?>, correctShortAnswerIndices: Set<Int>) {
    val correctIndices = quiz.questions.indices.filter { i ->
        when (val question = quiz.questions[i]) {
            is ShortAnswer -> correctShortAnswerIndices.contains(i)
            else -> question.answer == answers[i]
        }
    }
    val amountCorrect = correctIndices.size
    Centered {
        LazyColumn {
            item {
                Card {
                    Text(
                        "You answered $amountCorrect out of ${quiz.questions.size} correctly (${(amountCorrect.toDouble() / quiz.questions.size.toDouble()) * 100}%)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
            for (i in quiz.questions.indices) {
                if (i in correctIndices) {
                    continue
                }
                val question = quiz.questions[i]
                item {
                    Spacer(modifier = Modifier.padding(3.dp))
                    Card {
                        Column {
                            Text(question.question, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            when (question) {
                                is ShortAnswer -> {
                                    Row {
                                        Text(
                                            "Your answer: ",
                                            fontWeight = FontWeight.Bold
                                        ); Text(answers[i] as String)
                                    }
                                    Row { Text("Actual answer: ", fontWeight = FontWeight.Bold); Text(question.answer) }
                                }

                                is OptionsQuestion -> {
                                    Row {
                                        Column {
                                            Text("Your Answer:")
                                            Spacer(modifier=Modifier.padding(2.dp))
                                            ReportOptionsCard(
                                                question = question,
                                                show = if (question is ManyChoice) answers[i] as Set<Int> else setOf(
                                                    answers[i] as Int
                                                )
                                            )
                                        }
                                        Column {
                                            Text("Correct Answer:")
                                            Spacer(modifier = Modifier.padding(2.dp))
                                            ReportOptionsCard(
                                                question = question,
                                                show = if (question is ManyChoice) question.answer else setOf(
                                                    question.answer as Int
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}