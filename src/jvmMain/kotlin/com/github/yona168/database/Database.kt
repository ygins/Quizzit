package com.github.yona168.database

import com.github.yona168.questions.Quiz
import com.github.yona168.questions.SimpleMeta
import java.nio.file.Path
import java.util.*

/**
 * Controls saving/loading quizzes from a persistent database
 */
interface Database {
    /**
     * Saves the quiz
     * @param[quiz] - The quiz to be saved
     */
    suspend fun save(quiz: Quiz)

    /**
     * Loads the quiz from the database with the matching UUID
     * @param[quizId] - The quiz id to load
     * @return - The loaded [Quiz]
     */
    suspend fun load(quizId: UUID): Quiz

    /**
     * Deletes the quiz from the database with the matching UUID
     * @param[quizId] - The quiz id to delete
     */
    suspend fun delete(quizId: UUID)

    /**
     * Lists the [SimpleMeta] of all quizzes
     * @return a [List] containing the metadata of all quizzes in the database
     */
    suspend fun listQuizzes():List<SimpleMeta>
}

/**
 * An interface providing a skeletal backbone for a file-based [Database]
 * Using [QuizDataFolder], this interface implements a 2 file solution for each quiz. Namely, that for each quiz,
 * there's 1) a file storing the questions, and 2) a file storing the metadata. This separation allows for efficiency in
 * the [listQuizzes] function - not all the question data has to be loaded whenever that is called.
 *
 * This interface should be implemented to create a new file database that works with the above principles. If you
 * want to create one that works differently, implement [Database]
 */
interface FileDatabase : Database {
    /**
     * The data folder that contains all the data
     */
    val dataFolder: Path

    override suspend fun load(quizId: UUID): Quiz = load(getQuizFolder(quizId))

    /**
     * Loads the quiz in the given [QuizDataFolder]
     * @param[saveFolder] - the [QuizDataFolder] to load the quiz from
     * @return - The loaded [Quiz]
     */
    suspend fun load(saveFolder: QuizDataFolder): Quiz

    /**
     * Creates a [QuizDataFolder] representation of a quiz in the file system
     * @param[quizId] - The quiz to get the data folder for
     * @return - the [QuizDataFolder] containing the quiz
     */
    fun getQuizFolder(quizId: UUID) = object : QuizDataFolder {
        override val quizFolder = dataFolder.resolve(quizId.toString())
        override val metadata: Path
            get() = quizFolder.resolve("data.json")
        override val questions: Path
            get() = quizFolder.resolve("questions.json")
    }

    /**
     * A representation of a quiz within the file system
     */
    interface QuizDataFolder {
        /**
         * A [Path] representing where the folder containing the quiz data is
         */
        val quizFolder: Path

        /**
         * A [Path] representing where the folder containing the quiz questions is
         */
        val questions: Path

        /**
         * A [Path] representing where the folder containing the quiz metadata is
         */
        val metadata: Path
    }
}
