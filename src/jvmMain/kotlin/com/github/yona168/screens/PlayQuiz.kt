package com.github.yona168.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.yona168.database.Database
import com.github.yona168.questions.*
import kotlinx.coroutines.runBlocking
import java.util.*

@Composable
fun PlayQuiz(database: Database, quizId: UUID) {
    var quiz: Quiz? by remember{mutableStateOf(null)}
    val answers=remember{ mutableStateListOf<Any>() }
    runBlocking {
        quiz = database.load(quizId)
    }
    if(quiz!=null){
        for(i in quiz!!.questions.indices){
            val question = quiz!!.questions[i]
            val answerWith: (Any)->Unit = {givenAnswer->answers[i]=givenAnswer}
            when(question){
                is ShortAnswer-> ShortAnswerQuestion(question, answerWith)
            }
        }
    }
}

@Composable
fun CommonQuestion(question: Question, content: @Composable () -> Unit) {
    Card{
        Column{
            Text(question.question, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier= Modifier.padding(3.dp))
            content()
        }
    }
}

@Composable
fun ShortAnswerQuestion(question: ShortAnswer, answerWith: (Any)->Unit) = CommonQuestion(question) {
    OutlinedTextField("Your answer here", onValueChange = {
        answerWith(it)
    })
}

@Composable
fun MultipleChoiceQuestion(question: MultipleChoice, answerWith: (Any) -> Unit) = CommonQuestion(question) {

}

@Composable
fun ManyChoiceQuestion(question: ManyChoice, answerWith: (Any) -> Unit) = CommonQuestion(question) {

}