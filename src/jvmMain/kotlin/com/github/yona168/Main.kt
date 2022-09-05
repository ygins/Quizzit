package com.github.yona168// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.yona168.database.Database
import com.github.yona168.database.SimpleFileDatabase
import com.github.yona168.screens.Home


@Composable
@Preview
fun App() {
    var loaded by remember { mutableStateOf(false) }
    var currentFunc by remember { mutableStateOf<@Composable () -> Unit>({}) }
    val database: Database = remember { SimpleFileDatabase() }

    @Composable
    fun GoHome() {
        Home(changeScreen = { currentFunc = it }, database = database)
    }
    if (!loaded) {
        currentFunc = { GoHome() }
        loaded = true
    }
    Theme {
        Common(goHome = { currentFunc = { GoHome() } }) {
            currentFunc()
        }

    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

@Composable
fun Common(goHome: () -> Unit, content: @Composable () -> Unit) {
    Row(modifier = Modifier.fillMaxSize().background(Theme.colors.primary)) {
        Column(modifier = Modifier.background(Theme.colors.secondary).fillMaxHeight().width(50.dp)) {
            IconButton(onClick = goHome) {
                Icon(Icons.Filled.Home, contentDescription = "Home")
            }
        }
        content()
    }
}