package com.gradproj.optibodyai.starter_activity.screens

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.gradproj.optibodyai.components.EllipseHorizontal
import com.gradproj.optibodyai.components.ScreenHeader
import com.gradproj.optibodyai.main_activity.MainActivity
import com.gradproj.optibodyai.ui.theme.OptibodyAITheme
import com.gradproj.optibodyai.ui.theme.Secondary

@Immutable
data class Section(val question: String, val options: List<String>)

val Sections = listOf(
    Section(
        question = "Are you injured in any areas of your body?",
        options = listOf("Back", "Knee", "Shoulders")
    ),
    Section(
        question = "Do you suffer from any food allergies?",
        options = listOf("Peanut", "Seafood/Shellfish", "Wheat", "Milk and/or eggs", "Sesame")
    )
)

@Composable
fun ProfileDataFillingContinueScreen(navController: NavHostController) {
    Box {
        EllipseHorizontal()
        Column {

            ScreenHeader(
                navController,
                "Almost there, we just need a bit more information",
            )
            Spacer(modifier = Modifier.height(18.dp))

            Sections.forEachIndexed { index, section ->
                CheckSection(section.question, section.options)
                if (index != Sections.size - 1) {
                    HorizontalDivider(color = Secondary)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            val starterActivity = LocalContext.current

            Button(
                onClick = {
                    val intent = Intent(starterActivity, MainActivity::class.java)
                    starterActivity.startActivity(intent)
                },
                modifier = Modifier
                    .align(CenterHorizontally)
                    .fillMaxWidth(0.75f)
            ) {
                Text(text = "Save", fontSize = 15.sp)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun CheckSection(question: String, items: List<String>) {
    Column(
        Modifier
            .padding(horizontal = 6.dp)
            .padding(bottom = 10.dp)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .align(CenterHorizontally),
            text = question,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
        )

        items.forEach { item -> CheckBoxComponent(item) }
    }
}

@Composable
private fun CheckBoxComponent(text: String) {
    var checked by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(
            interactionSource = interactionSource, indication = null
        ) {
            checked = !checked
        }
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { checked = it },
            colors = CheckboxDefaults.colors(
                uncheckedColor = Secondary
            )
        )
        Text(text = text)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun ProfileDataFillingContinueScreenPreview() {
    OptibodyAITheme {
        Scaffold {
            ProfileDataFillingContinueScreen(navController = rememberNavController())
        }
    }
}