package com.example.choibase.ui.custom

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.choibase.ui.theme.OnPrimary
import com.example.choibase.ui.theme.PastelBlue
import com.example.choibase.ui.theme.PastelRed

sealed class ButtonType {
    data object Primary : ButtonType()
    data object Secondary : ButtonType()
    data object Danger : ButtonType()
}

@Preview
@Composable
fun testSolidButton() {
    SolidButton("테스트 버튼", ButtonType.Primary, {})
}

@Composable
fun SolidButton(
    text: String,
    type: ButtonType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonColor = when (type) {
        ButtonType.Primary -> ButtonDefaults.buttonColors(
            containerColor = PastelBlue,
            contentColor = OnPrimary
        )

        ButtonType.Secondary -> ButtonDefaults.buttonColors(
            containerColor = OnPrimary,
            contentColor = PastelBlue,
        )

        ButtonType.Danger -> ButtonDefaults.buttonColors(
            containerColor = PastelRed,
            contentColor = OnPrimary
        )
    }

    val buttonShape = RoundedCornerShape(8.dp)

    Button(
        colors = buttonColor,
        shape = buttonShape,
        border = if (type == ButtonType.Secondary) BorderStroke(1.dp, PastelBlue) else null,
        contentPadding = PaddingValues(8.dp),
        onClick = onClick,
        modifier = modifier
            .padding(4.dp)
            .heightIn(min = 50.dp),
    ) {
        Text(
            text = text,
            maxLines = 2,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            fontSize = 10.sp
        )
    }
}