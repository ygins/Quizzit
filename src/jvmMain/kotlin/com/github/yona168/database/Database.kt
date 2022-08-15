package com.github.yona168.database

import com.github.yona168.questions.Quiz
import com.github.yona168.questions.SimpleMeta
import java.nio.file.Path
import java.util.*

interface Database {
    suspend fun save(quiz: Quiz)
    suspend fun load(quizId: UUID): Quiz
    suspend fun listQuizzes():List<SimpleMeta>
}

interface FileDatabase : Database {
    val dataFolder: Path
    override suspend fun save(quiz: Quiz)
    override suspend fun load(quizId: UUID): Quiz = load(getQuizFolder(quizId))

    suspend fun load(saveFolder: QuizDataFolder): Quiz

    fun getQuizFolder(quizId: UUID) = object : QuizDataFolder {
        override val quizFolder = dataFolder.resolve(quizId.toString())
        override val data: Path
            get() = quizFolder.resolve("data.json")
        override val questions: Path
            get() = quizFolder.resolve("questions.json")
    }

    interface QuizDataFolder {
        val quizFolder: Path
        val questions: Path
        val data: Path
    }
}
