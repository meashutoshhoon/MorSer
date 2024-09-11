package com.jb.morser

import android.os.Bundle
import android.view.HapticFeedbackConstants
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jb.morser.App.Companion.applicationScope
import com.jb.morser.ui.enums.ThemeMode
import com.jb.morser.ui.model.ThemeModel
import com.jb.morser.ui.theme.MorSerTheme
import com.jb.morser.ui.util.Converter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val themeModel: ThemeModel by viewModels()

        setContent {
            MorSerTheme(
                when (themeModel.themeMode) {
                    ThemeMode.SYSTEM -> isSystemInDarkTheme()
                    ThemeMode.DARK -> true
                    else -> false
                }
            ) {

                val isDarkTheme = themeModel.themeMode == ThemeMode.DARK
                val view = LocalView.current

                val themeShifter = {
                    applicationScope.launch(Dispatchers.IO) {
                        val newThemeMode = if (isDarkTheme) ThemeMode.LIGHT else ThemeMode.DARK
                        themeModel.updateThemeMode(newThemeMode)  // Update the theme mode using the new function
                    }
                }

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(topBar = {
                        CenterAlignedTopAppBar(title = { Text(text = "MorSer") }, actions = {
                            IconButton(
                                onClick = {
                                    view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                                    themeShifter()
                                }
                            ) {
                                Icon(
                                    imageVector = if (isDarkTheme) Icons.Rounded.DarkMode else Icons.Rounded.LightMode,
                                    contentDescription = "Theme Change"
                                )
                            }
                        })
                    }) {
                        CenterScreen(
                            modifier = Modifier.padding(it)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CenterScreen(
    modifier: Modifier = Modifier
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val clipboardManager = LocalClipboardManager.current
    val hapticFeedback = LocalHapticFeedback.current

    var text by remember {
        mutableStateOf("")
    }

    var result by remember {
        mutableStateOf("")
    }

    var getCard by remember {
        mutableStateOf(false)
    }

    val convertToMorse = {
        result = Converter.alphaToMorse(text)
        keyboardController?.hide()
        getCard = true
    }

    val convertToText = {
        result = Converter.morseToAlpha(text)
        keyboardController?.hide()
        getCard = true
    }


    Column {

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = modifier
                .padding(8.dp, 25.dp, 8.dp, 10.dp)
                .navigationBarsPadding()
                .fillMaxWidth()
                .height(200.dp),
            label = { Text("Enter the text") },
            shape = RoundedCornerShape(16.dp)
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {

            Button(
                onClick = convertToMorse,
                modifier = Modifier
                    .padding(8.dp, 2.dp)
                    .weight(1f)
                    .wrapContentHeight()
            ) {
                Text(
                    text = "Convert to Morse"
                )
            }

            Button(
                onClick = convertToText,
                modifier = Modifier
                    .padding(8.dp, 2.dp)
                    .weight(1f)
                    .wrapContentHeight()
            ) {
                Text(
                    text = "Convert to Text"
                )
            }

        }

        AnimatedVisibility(
            visible = getCard,
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier
                    .padding(10.dp, 5.dp)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = result, modifier = Modifier
                    .padding(8.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(onLongPress = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            clipboardManager.setText(AnnotatedString(result))
                        })
                    })
            }
        }

    }
}