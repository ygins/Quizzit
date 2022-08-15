package com.github.yona168.objectequals

import com.github.yona168.questions.Quiz
import com.github.yona168.database.SimpleSerializer
import com.github.yona168.resources.manyChoice
import com.github.yona168.resources.multipleChoice
import com.github.yona168.resources.shortAnswer
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class SerializationTest {
    @Test
    fun serializeQuiz(){
        val quiz= Quiz("Quiz 1", "Yona168", shortAnswer,manyChoice, multipleChoice)
        val serializer= SimpleSerializer()
        val serialized=serializer.serialize(quiz)
        val deserialized=serializer.deserialize(serialized)
        assertEquals(quiz, deserialized)
        assertNotEquals(Quiz("Quiz 1", "Yona168", multipleChoice, manyChoice, shortAnswer), deserialized)
    }
}