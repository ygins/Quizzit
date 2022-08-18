package com.github.yona168.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.yona168.Centered
import com.github.yona168.database.Database
import com.github.yona168.questions.*
import kotlinx.coroutines.runBlocking
import java.util.*

@Composable
fun PlayQuiz(database: Database, quizId: UUID) {
    var quiz: Quiz? by remember { mutableStateOf(null) }
    val answers: SnapshotStateList<Any?>
    val correctShortAnswerIndices by remember { mutableStateOf(mutableSetOf<Int>()) }
    var review by remember { mutableStateOf(false) }
    var report by remember { mutableStateOf(false) }
    runBlocking {
        quiz = database.load(quizId)
    }
    if (quiz != null) {
        answers = remember { mutableStateListOf(*arrayOfNulls(quiz!!.questions.size)) }
        if (!review) {
            FirstTake(quiz!!, answers) {
                review = true
                if (quiz!!.questions.none { it is ShortAnswer }) {
                    report = true
                }
            }
        } else if (!report) {
            ReviewShortAnswer(quiz!!, answers, correctShortAnswerIndices) { report = true }
        } else {
            FinalReport(quiz!!, answers, correctShortAnswerIndices)
        }
    }
}


@Composable
fun FirstTake(quiz: Quiz, answers: MutableList<Any?>, changeToReview: () -> Unit) {
    Centered {
        LazyColumn {
            item {
                Text("${quiz.name} by ${quiz.author}", fontWeight = FontWeight.Bold, fontSize = 27.sp)
                Spacer(modifier = Modifier.padding(3.dp))
            }
            items(quiz.questions.size) { i ->
                val question = quiz.questions[i]
                val answerWith: (Any) -> Unit = { givenAnswer -> answers[i] = givenAnswer }
                when (question) {
                    is ShortAnswer -> ShortAnswerQuestion(question, i, answers[i] as (String?), answerWith)
                    is MultipleChoice -> MultipleChoiceQuestion(question, i, answers[i] as (Int?), answerWith)
                    is ManyChoice -> ManyChoiceQuestion(
                        question, i,
                        (answers.let {
                            if (it[i] == null) {
                                it[i] = setOf<Int>()
                            }
                            it[i] as Set<Int>
                        }), answerWith
                    )
                }
                Spacer(modifier = Modifier.padding(3.dp))
            }
            item {
                OutlinedButton(onClick = {
                    if (answers.all { it != null }) {
                        changeToReview()
                    }
                }) {
                    Text(if (answers.all { it != null }) "Submit" else "Answer all questions before submitting")
                }
            }
        }
    }
}

@Composable
fun ReviewShortAnswer(quiz: Quiz, answers: List<Any?>, correctIndices: MutableSet<Int>, moveOnToReport: () -> Unit) {
    val shortAnswerIndices = remember { quiz.questions.indices.filter { quiz.questions[it] is ShortAnswer } }
    val removed = remember { mutableStateListOf<Int>() }
    Centered {
        LazyColumn {
            items(shortAnswerIndices.size) { i ->
                if (i !in removed) {
                    val question = quiz.questions[i] as ShortAnswer
                    Column {
                        Text("Question ${i + 1}: ${question.question}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Your answer:", fontWeight = FontWeight.Bold)
                        Text(answers[i] as String)
                        Text("Correct answer:", fontWeight = FontWeight.Bold)
                        Text(question.answer)
                        Text("Did you get it right?", fontWeight = FontWeight.Bold)
                        Row {
                            val checkIfDone = { if (removed.size == shortAnswerIndices.size) moveOnToReport() }
                            OutlinedButton(onClick = {
                                removed += i
                                correctIndices += i
                                checkIfDone()
                            }) {
                                Text("Yes!")
                            }
                            Spacer(modifier = Modifier.padding(3.dp))
                            OutlinedButton(onClick = {
                                removed += i
                                checkIfDone()
                            }) {
                                Text("Ill get it next time")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.padding(3.dp))
                }
            }
        }
    }
}

@Composable
fun FinalReport(quiz: Quiz, answers: List<Any?>, correctShortAnswerIndices: Set<Int>) {
    val correctIndices = quiz.questions.indices.filter { i ->
        val question = quiz.questions[i]
        when (question) {
            is ShortAnswer -> correctShortAnswerIndices.contains(i)
            else -> question.answer == answers[i]
        }
    }
    val amountCorrect = correctIndices.size
    Centered {
        Card {
            Text(
                "You answered $amountCorrect out of ${quiz.questions.size} correctly (${amountCorrect.toDouble() / quiz.questions.size.toDouble()}%)",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }

}

@Composable
fun CommonQuestion(question: Question, index: Int, content: @Composable () -> Unit) {
    Centered {
        Card {
            Column {
                Text("Question ${index + 1}: ${question.question}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                content()
            }
        }
    }
}

@Composable
fun ShortAnswerQuestion(question: ShortAnswer, index: Int, currentAnswer: String?, answerWith: (Any) -> Unit) =
    CommonQuestion(question, index) {
        OutlinedTextField(currentAnswer ?: "Your answer here", onValueChange = {
            answerWith(it)
        }, modifier = Modifier.fillMaxWidth())
    }

@Composable
fun MultipleChoiceQuestion(question: MultipleChoice, index: Int, currentAnswer: Int?, answerWith: (Any) -> Unit) =
    CommonQuestion(question, index) {
        Column {
            for (i in question.options.indices) {
                Row {
                    RadioButton(selected = currentAnswer == i, onClick = {
                        answerWith(i)
                    })
                    Text("\n${question.options[i]}")
                }
                Spacer(modifier = Modifier.padding(1.dp))
            }
        }
    }

@Composable
fun ManyChoiceQuestion(question: ManyChoice, index: Int, currentAnswer: Set<Int>, answerWith: (Any) -> Unit) =
    CommonQuestion(question, index) {
        Column {
            Text("Check all that apply", fontStyle = FontStyle.Italic, color = Color.Gray)
            for (i in question.options.indices) {
                Row {
                    RadioButton(selected = currentAnswer.contains(i), onClick = {
                        val newSet = mutableSetOf<Int>()
                        newSet.addAll(currentAnswer)
                        if (currentAnswer.contains(i)) {
                            newSet -= i
                        } else {
                            newSet += i
                        }
                        answerWith(newSet)
                    })
                    Text("\n${question.options[i]}")
                }
            }
        }
    }