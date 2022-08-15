package com.github.yona168.database

import com.github.yona168.questions.Quiz
import com.github.yona168.questions.SimpleMeta
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.util.*

data class SerializedQuiz(val metadata: String, val questions: String)

interface Serializer {
    fun serialize(quiz: Quiz)=SerializedQuiz(Json.encodeToString(quiz.data), Json.encodeToString(quiz.questions))
    fun deserialize(serialized: SerializedQuiz)= Quiz(Json.decodeFromString<SimpleMeta>(serialized.metadata), Json.decodeFromString(serialized.questions))
    fun deserializeMetaData(serialized: String)=Json.decodeFromString<SimpleMeta>(serialized)
}
class SimpleSerializer: Serializer

object UUIDAsStringSerializer : KSerializer<UUID> {
    override val descriptor = PrimitiveSerialDescriptor("ID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UUID = UUID.fromString(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: UUID) = encoder.encodeString(value.toString())

}
