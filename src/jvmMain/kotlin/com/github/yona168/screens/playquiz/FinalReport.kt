package com.github.yona168.screens.playquiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.github.yona168.BoldText
import com.github.yona168.Centered
import com.github.yona168.SmallSpacer
import com.github.yona168.questions.ManyChoice
import com.github.yona168.questions.OptionsQuestion
import com.github.yona168.questions.Quiz
import com.github.yona168.questions.ShortAnswer

/**
 * Handles showing the final report for how the user did
 */
@Composable
fun FinalReport(quiz: Quiz, answers: List<Any?>, correctShortAnswerIndices: Set<Int>) {
    val correctIndices = quiz.questions.indices.filter { i -> //All the indices of questions that the user got correct
        when (val question = quiz.questions[i]) {
            is ShortAnswer -> correctShortAnswerIndices.contains(i)
            else -> question.answer == answers[i]
        }
    }
    val amountCorrect = correctIndices.size
    Centered {
        LazyColumn {
            item {
                BoldText(
                    "You answered $amountCorrect out of ${quiz.questions.size} correctly (${(amountCorrect.toDouble() / quiz.questions.size.toDouble()) * 100}%)\n" +
                            "Your answers are shown below",
                    fontSize = 25.sp
                )
            }
            for (i in quiz.questions.indices) {
                fun isCorrect() = i in correctIndices
                val question = quiz.questions[i]
                item {
                    SmallSpacer()
                    Row {
                        Card(modifier = Modifier.weight(7f)) {
                            Column {
                                BoldText("Question ${i + 1}: ${question.question}", fontSize = 20.sp)
                                when (question) {
                                    is ShortAnswer -> {
                                        Row {
                                            BoldText(
                                                "Your answer: "
                                            ); Text(answers[i] as String)
                                        }
                                        SmallSpacer()
                                        Row { BoldText("Correct answer: "); Text(question.answer) }
                                    }

                                    is OptionsQuestion -> {
                                        Row {
                                            Column {
                                                BoldText("Your Answer:")
                                                SmallSpacer()
                                                ReportOptionsCard(
                                                    question = question,
                                                    show = if (question is ManyChoice) answers[i] as Set<Int> else setOf(
                                                        answers[i] as Int
                                                    )
                                                )
                                            }
                                            SmallSpacer()
                                            Column {
                                                BoldText("Correct Answer:")
                                                SmallSpacer()
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
                        SmallSpacer()
                        CorrectText(isCorrect(), modifier = Modifier.weight(1.5f))
                    }
                }
            }
        }

    }
}

/**
 * If the question is [ManyChoice] or [MultipleChoiceQuestion], this creates a view showing the incorrect vs correct answers
 */
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
fun CorrectText(correct: Boolean, modifier: Modifier = Modifier) {
    BoldText(
        if (correct) "Correct!" else "Incorrect",
        20.sp,
        color = if (correct) Color.Green else Color.Red,
        modifier = modifier
    )
}