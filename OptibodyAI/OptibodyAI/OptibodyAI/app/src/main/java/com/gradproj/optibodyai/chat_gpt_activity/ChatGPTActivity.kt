package com.gradproj.optibodyai.chat_gpt_activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gradproj.optibodyai.ui.theme.OptibodyAITheme

class ChatGPTActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OptibodyAITheme {
                ChatGPTScreen()
            }
        }
    }
}
