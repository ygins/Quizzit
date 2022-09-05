package com.github.yona168.database

import com.github.yona168.questions.Quiz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.exists

/**
 * A simple implementation of [FileDatabase]
 */
class SimpleFileDatabase : FileDatabase {
    private val serializer = SimpleSerializer()
    override val dataFolder: Path = Paths.get(System.getProperty(".Quizzit"), "quizzes")

    init {
        if (!dataFolder.exists()) {
            Files.createDirectory(dataFolder)
        }
    }

    override suspend fun save(quiz: Quiz) {
        val quizPathData = getQuizFolder(quiz.id)
        val saveFolder = quizPathData.quizFolder
        val serialized = serializer.serialize(quiz)
        if (!saveFolder.exists()) {
            withContext(Dispatchers.IO) {
                Files.createDirectory(saveFolder)
            }
        }
        withContext(Dispatchers.IO) {
            Files.writeString(quizPathData.metadata, serialized.metadata)
            Files.writeString(quizPathData.questions, serialized.questions)
        }
    }

    override suspend fun delete(quizId: UUID) = withContext(Dispatchers.IO) {
        val dataFolder = getQuizFolder(quizId)
        listOf(dataFolder.questions, dataFolder.metadata, dataFolder.quizFolder).forEach(Files::delete)
    }

    override suspend fun load(saveFolder: FileDatabase.QuizDataFolder) = withContext(Dispatchers.IO) {
        val serialized = SerializedQuiz(
            metadata = Files.readString(saveFolder.metadata),
            questions = Files.readString(saveFolder.questions)
        )
        return@withContext serializer.deserialize(serialized)
    }

    override suspend fun listQuizzes() = withContext(Dispatchers.IO) {
        Files.list(dataFolder).map { it.resolve("data.json") }.map(Files::readString)
            .map(serializer::deserializeMetaData).toList()
    }
}