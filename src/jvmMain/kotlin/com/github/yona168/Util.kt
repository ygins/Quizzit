package com.github.yona168

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

val Theme = MaterialTheme

@Composable
fun Theme(content: @Composable () -> Unit) = MaterialTheme { content() }

//val Colorz = Theme.colors

@Composable
fun Centered(content: @Composable () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        content()
    }
}

@Composable
fun LeftColumn(content: @Composable () -> Unit) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        content()
    }
}

@Composable
fun BoldText(text: String, fontSize: TextUnit = TextUnit.Unspecified) = Text(text, fontWeight = FontWeight.Bold, fontSize = fontSize)

@Composable
fun SmallSpacer() = Spacer(modifier=Modifier.padding(3.dp))

fun <T> List<T>.copyAndSetAt(newValue: T, index: Int): List<T>{
    val newList = mutableListOf<T>()
    newList.addAll(this)
    newList.removeAt(index)
    newList.add(index, newValue)
    return this
}

fun <T> List<T>.copyAndRemoveAt(index: Int): List<T>{
    val newList = mutableListOf<T>()
    newList.addAll(this)
    newList.removeAt(index)
    return this
}
