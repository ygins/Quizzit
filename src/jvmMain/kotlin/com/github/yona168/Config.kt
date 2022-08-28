package com.github.yona168

import androidx.compose.ui.res.useResource
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.nio.file.Path
import java.nio.file.Paths

interface ConfigInfo {
    val fileDatabasePath: Path
}

class Config : ConfigInfo by Json.decodeFromString(
    SimpleConfigInfo.serializer(),
    useResource("config.json") {
        it.bufferedReader().readText()
    }
) {

    @Serializable
    private class SimpleConfigInfo(@Serializable(with = PathAsStringSerializer::class) override val fileDatabasePath: Path) :
        ConfigInfo

    private class PathAsStringSerializer : KSerializer<Path> {
        override val descriptor = PrimitiveSerialDescriptor("PATH", PrimitiveKind.STRING)
        override fun deserialize(decoder: Decoder): Path = Paths.get(decoder.decodeString())
        override fun serialize(encoder: Encoder, value: Path) = encoder.encodeString(value.toString())

    }
}


