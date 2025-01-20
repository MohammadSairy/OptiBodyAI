package com.gradproj.optibodyai.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gradproj.optibodyai.ui.theme.Secondary


@Composable
fun TextFieldComponent(headerName: String, placeholder: String) {
    var textFieldValue by remember { mutableStateOf("") }
    Spacer(modifier = Modifier.height(10.dp))
    Text(text = headerName, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(10.dp))
    OutlinedTextField(
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Secondary,
            unfocusedBorderColor = Secondary
        ),
        shape = RoundedCornerShape(8.dp),
        value = textFieldValue,
        modifier = Modifier.fillMaxWidth(),
        onValueChange = { textFieldValue = it },
        placeholder = { Text(text = placeholder) }
    )
}
