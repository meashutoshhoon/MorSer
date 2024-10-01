package com.jb.morser.ui.util

import java.util.Locale

class Converter {
    companion object {
        private val ALPHA = arrayOf(
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
            "s", "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "!", ",", "?",
            ".", "'"
        )
        private val MORSE = arrayOf(
            ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..",
            "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..", ".----",
            "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----.", "-----", "-.-.--", "--..--",
            "..--..", ".-.-.-", ".----."
        )

        private val ALPHA_TO_MORSE = hashMapOf<String, String>()
        private val MORSE_TO_ALPHA = hashMapOf<String, String>()

        init {
            for (i in ALPHA.indices) {
                ALPHA_TO_MORSE[ALPHA[i]] = MORSE[i]
                MORSE_TO_ALPHA[MORSE[i]] = ALPHA[i]
            }
        }

        fun morseToAlpha(morseCode: String): String {
            val builder = StringBuilder()
            val words = morseCode.trim().split("   ")

            for (word in words) {
                for (letter in word.split(" ")) {
                    val alpha = MORSE_TO_ALPHA[letter]
                    builder.append(alpha)
                }
                builder.append(" ")
            }

            return builder.toString().uppercase(Locale.getDefault())
        }

        fun alphaToMorse(englishCode: String): String {
            val builder = StringBuilder()
            val words = englishCode.trim().split(" ")

            for (word in words) {
                for (i in word.indices) {
                    val morse = ALPHA_TO_MORSE[word.substring(i, i + 1).lowercase(Locale.getDefault())]
                    builder.append("$morse ")
                }
                builder.append("  ")
            }

            return builder.toString()
        }
    }
}