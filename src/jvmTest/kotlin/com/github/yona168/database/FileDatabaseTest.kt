package com.github.yona168.database

import com.github.yona168.questions.Quiz
import com.github.yona168.resources.manyChoice
import com.github.yona168.resources.multipleChoice
import com.github.yona168.resources.shortAnswer
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

internal class FileDatabaseTest {
    private val database = SimpleFileDatabase()

    @Test
    fun saveAndLoad() = runBlocking {
        val quiz = Quiz("Quiz 123", "Yona168", manyChoice, shortAnswer, multipleChoice)
        database.save(quiz)
        val loaded = database.load(quiz.id)
        assertEquals(quiz, loaded)
    }
}