package com.github.yona168.objectequals

import com.github.yona168.questions.Quiz
import com.github.yona168.questions.SimpleMeta
import com.github.yona168.resources.manyChoice
import com.github.yona168.resources.multipleChoice
import com.github.yona168.resources.shortAnswer
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class CompareQuiz {
    private val quiz = Quiz("Quiz A", "Yona168", multipleChoice, shortAnswer)

    @Test
    fun identity() {
        val quizTwo = Quiz(quiz.data, quiz.questions)
        assertEquals(quiz, quizTwo)
    }

    @Test
    fun differentID() {
        val otherQuiz= Quiz(SimpleMeta(quiz.name, quiz.author), quiz.questions)
        assertNotEquals(quiz, otherQuiz)
    }

    @Test
    fun differentQuestions(){
        val otherQuiz= Quiz(quiz.data, listOf(manyChoice, manyChoice, manyChoice))
        assertNotEquals(quiz, otherQuiz)
    }
}