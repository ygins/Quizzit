package com.github.yona168.questions

import com.github.yona168.database.UUIDAsStringSerializer
import kotlinx.serialization.Serializable
import java.util.*

typealias BooleanOption = Pair<String, Boolean>
typealias Author = String


sealed interface QuizMeta {
    val name: String
    val author: Author
    val id: UUID
}

@Serializable
data class SimpleMeta(
    override val name: String,
    override val author: String,
    @Serializable(with = UUIDAsStringSerializer::class) override val id: UUID = UUID.randomUUID()
) : QuizMeta

@Serializable
data class Quiz(
    val data: SimpleMeta,
    val questions: List<Question>
) : QuizMeta by data {
    constructor(name: String, author: String, vararg questions: Question) : this(
        SimpleMeta(name, author),
        questions.toList()
    )
}

@Serializable
sealed class Question {
    abstract val question: String
    abstract val answer: Any
}

@Serializable
data class ShortAnswer(override val question: String, override val answer: String) : Question()

@Serializable
sealed class OptionsQuestion : Question() {
    abstract val options: List<String>
}

@Serializable
data class MultipleChoice(override val question: String, override val options: List<String>, override val answer: Int) :
    OptionsQuestion() {
    constructor(question: String, options: List<BooleanOption>) : this(
        question,
        options.map { it.first },
        options.withIndex().find { it.value.second }!!.index
    )
}

/*
Goals for this class: Need to be able to:
1) Iterate over each option with its boolean pair
2) Based on a clicked option, change its answer
 */
@Serializable
data class ManyChoice(
    override val question: String, val optionsAndAnswers: List<BooleanOption>
) : OptionsQuestion() {
    constructor(question: String, options: List<String>, answer: List<Int>) : this(
        question,
        options.mapIndexed { index, str -> str to answer.contains(index) }
    )

    constructor(question: String, map: Map<String, Boolean>) : this(
        question,
        map.entries.toList().map { it.key to it.value })

    override val answer: List<Int>
        get() = optionsAndAnswers.mapIndexed { index, pair -> if (pair.second) index else -1 }.filter { it != -1 }
    override val options: List<String>
        get() = optionsAndAnswers.map { it.first }
    val answerBooleans: Array<Boolean>
        get() = Array(optionsAndAnswers.size) { optionsAndAnswers[it].second }
    val optionsAndAnswersMap: Map<String, Boolean>
        get() = mutableMapOf(*optionsAndAnswers.toTypedArray())
}