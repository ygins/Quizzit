package com.github.yona168.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.yona168.BoldText
import com.github.yona168.Centered
import com.github.yona168.copyAndRemoveAt
import com.github.yona168.copyAndSetAt
import com.github.yona168.database.Database
import com.github.yona168.questions.*
import kotlinx.coroutines.runBlocking
import java.util.*

/**
 * Handles editing a quiz
 */
@Composable
fun EditQuiz(database: Database, goHome: () -> Unit, quizToLoad: UUID? = null) {
    var titleInput by remember { mutableStateOf("Title") }
    var authorInput by remember { mutableStateOf("Author") }
    val questions = remember { mutableStateListOf<Question>() }
    var quizId by remember { mutableStateOf<UUID?>(null) }
    if (quizToLoad != null && quizId == null) { //Loads the input quiz from the database
        val inputQuiz: Quiz
        runBlocking {
            inputQuiz = database.load(quizToLoad)
        }
        titleInput = inputQuiz.name
        authorInput = inputQuiz.author
        quizId=inputQuiz.id
        questions.addAll(inputQuiz.questions)
    } else if(quizToLoad==null && quizId==null){ //Creating a new quiz
        quizId=UUID.randomUUID()
    }
    Column {
        TitleAndAuthorInput(
            titleInput,
            changeTitle = { titleInput = it },
            authorInput,
            changeAuthor = { authorInput = it })
        Centered {
            LazyColumn(modifier = Modifier.padding(10.dp)) {
                itemsIndexed(questions) { i, item ->
                    val removeQuestion: () -> Unit = { questions.removeAt(i) }
                    val alterQuestion: (Question) -> Unit =
                        { newQuestion -> removeQuestion(); questions.add(i, newQuestion) }
                    when (item) {
                        is ShortAnswer -> ShortAnswerCard(item, i, removeQuestion, alterQuestion)
                        is MultipleChoice -> MultipleChoiceCard(item, i, removeQuestion, alterQuestion)
                        is ManyChoice -> ManyChoiceCard(item, i, removeQuestion, alterQuestion)
                    }
                }
                item {
                    AddQuestionBar { questions += it }
                }
                item {
                    SaveQuizButton(titleInput, authorInput, questions, database, afterSave = goHome, quizId)
                }
            }
        }
    }
}

@Composable
fun TitleAndAuthorInput(title: String, changeTitle: (String) -> Unit, author: String, changeAuthor: (String) -> Unit) {
    Row {
        TextField(title, onValueChange = changeTitle)
        Spacer(modifier = Modifier.padding(5.dp))
        TextField(author, onValueChange = changeAuthor)
    }
}

/**
 * Displays the buttons that add questions
 */
@Composable
fun AddQuestionBar(addQuestion: (Question) -> Unit) {
    Row {
        OutlinedButton(onClick = { addQuestion(ShortAnswer("Question", "Answer")) }) {
            Text("Add Short Answer")
        }
        OutlinedButton(onClick = {
            addQuestion(
                ManyChoice(
                    "Question",
                    listOf("This is an answer" to true, "This is not an answer" to false)
                )
            )
        }) {
            Text("Add Many Choice")
        }
        OutlinedButton(onClick = { addQuestion(MultipleChoice("Question", listOf("False", "True"), 1)) }) {
            Text("Add Multiple Choice")
        }
    }
}

@Composable
fun QuestionCard(index: Int, removeQuestion: () -> Unit, style: String, content: @Composable () -> Unit) {
    Card(modifier = Modifier.padding(vertical = 8.dp)) {
        Row {
            Column(modifier = Modifier.weight(6f)) {
                BoldText("Question ${index + 1} - $style", fontSize = 20.sp)
                content()
            }
            IconButton(onClick = removeQuestion, modifier = Modifier.weight(1f)) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
fun ShortAnswerCard(question: ShortAnswer, index: Int, removeQuestion: () -> Unit, alterQuestion: (Question) -> Unit) =
    QuestionCard(index, removeQuestion, "Short Answer") {
        TextField(question.question, onValueChange = { alterQuestion(ShortAnswer(it, question.answer)) })
        TextField(question.answer, onValueChange = { alterQuestion(ShortAnswer(question.question, it)) })
    }

@Composable
fun MultipleChoiceCard(
    question: MultipleChoice,
    index: Int,
    removeQuestion: () -> Unit,
    alterQuestion: (Question) -> Unit
) = QuestionCard(index, removeQuestion, "Multiple Choice") {
    TextField(
        question.question,
        onValueChange = { alterQuestion(MultipleChoice(it, question.options, question.answer)) })
    for (i in 0 until question.options.size) {
        Row {
            TextField(question.options[i], onValueChange = {
                val newOptions = question.options.copyAndSetAt(it, i)
                alterQuestion(MultipleChoice(question.question, newOptions, question.answer))
            }, modifier = Modifier.weight(6f))
            OutlinedButton(onClick = {
                if (i != question.answer) {
                    alterQuestion(MultipleChoice(question.question, question.options, i))
                }
            }, modifier = Modifier.weight(1f)) {
                Text(if (question.answer == i) "True" else "False")
            }
            IconButton(onClick = {
                if (question.options.size > 1) {
                    val newOptions = question.options.copyAndRemoveAt(i)
                    var newAnswer = question.answer
                    if (newOptions.indices.contains(question.answer).not()) {
                        newAnswer = 0
                    }
                    alterQuestion(MultipleChoice(question.question, newOptions, newAnswer))
                }
            }) {
                Icon(Icons.Filled.Delete, "Remove Option")
            }
        }
    }
    OutlinedButton(onClick = {
        val newOptions = mutableListOf<String>()
        newOptions.addAll(question.options)
        newOptions += "False"
        alterQuestion(MultipleChoice(question.question, newOptions, question.answer))
    }) {
        Text("Add Option")
    }
}

@Composable
fun ManyChoiceCard(
    question: ManyChoice,
    index: Int,
    removeQuestion: () -> Unit,
    alterQuestion: (ManyChoice) -> Unit
) =
    QuestionCard(index, removeQuestion, "Many Choice") {
        TextField(
            question.question,
            onValueChange = { alterQuestion(ManyChoice(it, question.options, question.answer)) })
        for (i in 0 until question.options.size) {
            Row {
                TextField(question.options[i], onValueChange = {
                    val newOptions = question.options.copyAndSetAt(it, i)
                    alterQuestion(ManyChoice(question.question, newOptions, question.answer))
                }, modifier = Modifier.weight(6f))
                OutlinedButton(onClick = {
                    val newAnswers = question.answerBooleans
                    newAnswers[i] = newAnswers[i].not()
                    alterQuestion(ManyChoice(question.question, question.options.zip(newAnswers)))
                }, modifier = Modifier.weight(1f)) {
                    Text(if (question.answerBooleans[i]) "True" else "False")
                }
                IconButton(onClick = {
                    if (question.options.size > 1) {
                        val newPairs = question.optionsAndAnswers.copyAndRemoveAt(i)
                        alterQuestion(ManyChoice(question.question, newPairs))
                    }
                }) {
                    Icon(Icons.Filled.Delete, "Remove Option")
                }
            }
        }
        OutlinedButton(onClick = {
            val newOptions = mutableListOf<String>()
            newOptions.addAll(question.options)
            newOptions += "False"
            alterQuestion(ManyChoice(question.question, newOptions, question.answer))
        }) {
            Text("Add Option")
        }
    }

@Composable
fun SaveQuizButton(
    title: String,
    author: String,
    questions: List<Question>,
    database: Database,
    afterSave: () -> Unit,
    id: UUID? = null
) {
    OutlinedButton(onClick = {
        if (questions.isNotEmpty()) {
            val quiz = Quiz(SimpleMeta(title, author, id ?: UUID.randomUUID()), questions)
            runBlocking { database.save(quiz) }
            afterSave()
        }
    }) {
        Text(if (questions.isNotEmpty()) "Save Quiz" else "Please add at least one question")
    }
}
