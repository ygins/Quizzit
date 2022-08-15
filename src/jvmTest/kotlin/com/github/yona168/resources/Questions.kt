package com.github.yona168.resources

import com.github.yona168.questions.ManyChoice
import com.github.yona168.questions.MultipleChoice
import com.github.yona168.questions.ShortAnswer

internal val manyChoice= ManyChoice("Check all that are true", listOf(
    "Roses are yellow" to false,
    "Roses are red" to true,
    "Blue=Blue" to true,
    "The sky is yellow" to false,
    "The sky is blue" to true
))
internal val multipleChoice= MultipleChoice("Which one of the following is true?", listOf(
    "Roses are blue" to false,
    "The sky is blue" to true,
    "The sky is red" to false,
    "Roses are yellow" to false
))
internal val shortAnswer= ShortAnswer("What is the meaning of life?","42")