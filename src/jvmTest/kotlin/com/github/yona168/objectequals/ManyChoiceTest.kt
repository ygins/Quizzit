package com.github.yona168.objectequals

import com.github.yona168.resources.manyChoice
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

internal class ManyChoiceTest {
    @Test
    fun getAnswer() {
        assertEquals(listOf(1, 2, 4), manyChoice.answer)
    }
}