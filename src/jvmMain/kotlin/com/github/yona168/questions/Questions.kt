package com.github.yona168.questions

import com.github.yona168.database.UUIDAsStringSerializer
import kotlinx.serialization.Serializable
import java.util.*

typealias BooleanOption = Pair<String, Boolean>
typealias Author = String

/**
 * The non-question data of a quiz
 */
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

/**
 * Data object containing both the metadata and a list of questions
 */
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

/**
 * A question has a question and an answer
 */
@Serializable
sealed class Question {
    abstract val question: String
    abstract val answer: Any
}

/**
 * A short answer question is simple: Question is a string, and the answer is a string
 */
@Serializable
data class ShortAnswer(override val question: String, override val answer: String) : Question()

@Serializable
sealed class OptionsQuestion : Question() {
    abstract val options: List<String>
}

/**
 * A multiple choice question gives many options, with one correct answer, represented by the index of the correct answer in [options]
 */
@Serializable
data class MultipleChoice(override val question: String, override val options: List<String>, override val answer: Int) :
    OptionsQuestion() {
    constructor(question: String, options: List<BooleanOption>) : this(
        question,
        options.map { it.first },
        options.withIndex().find { it.value.second }!!.index
    )
}

/**
 * A many choice question can have many answers, represented by a boolean connected to that option in [optionsAndAnswers]
 */
@Serializable
data class ManyChoice(
    override val question: String, val optionsAndAnswers: List<BooleanOption>
) : OptionsQuestion() {
    constructor(question: String, options: List<String>, answer: Set<Int>) : this(
        question,
        options.mapIndexed { index, str -> str to answer.contains(index) }
    )

    override val answer: Set<Int>
        get() = optionsAndAnswers.mapIndexed { index, pair -> if (pair.second) index else -1 }.filter { it != -1 }.toSet()
    override val options: List<String>
        get() = optionsAndAnswers.map { it.first }
    val answerBooleans: Array<Boolean>
        get() = Array(optionsAndAnswers.size) { optionsAndAnswers[it].second }
}