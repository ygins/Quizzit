package com.github.yona168.objectequals

import com.github.yona168.resources.shortAnswer
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class ShortAnswerTest {
    @Test
    fun getQuestion() {
        assertEquals(shortAnswer.question, "What is the meaning of life?")
    }

    @Test
    fun getAnswer() {
        assertEquals(shortAnswer.answer, "42")
    }
}