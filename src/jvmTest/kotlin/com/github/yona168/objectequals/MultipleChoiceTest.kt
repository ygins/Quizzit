package com.github.yona168.objectequals


import com.github.yona168.resources.multipleChoice
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class MultipleChoiceTest {
    @Test
    fun getOptions() {
        assertEquals(listOf("Roses are blue","The sky is blue","The sky is red","Roses are yellow"), multipleChoice.options)
    }

    @Test
    fun getAnswer() {
        assertEquals(1, multipleChoice.answer)
    }
}