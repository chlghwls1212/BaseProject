package com.example.choibase.com.example.choibase.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.example.choibase.ui.custom.ButtonType
import com.example.choibase.ui.custom.SolidButton
import com.example.choibase.ui.theme.OnPrimary
import com.example.choibase.ui.theme.Typography


@Preview
@Composable
//권한용 다이얼로그 커스텀화면 프리뷰
fun PreviewPermissionDialog() {
    PermissionDialog({}, "example", "explains SomeThing, Test here any test Data write ", "확인", {}, "취소", {})
}


//권한용 다이얼로그 커스텀화면
@Composable
fun PermissionDialog(
    onDismissRequest: () -> Unit,
    title: String,
    text: String,
    confirmButton: String,
    onConfirm: () -> Unit,
    dismissButton: String? = null,
    onDismiss: (() -> Unit)? = null
) {
    //화면 너비 구하기
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val dialogWidth = max(screenWidth * 0.8f, 280.dp)

    val dialogShape = RoundedCornerShape(12.dp)

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Transparent)
        .clickable { onDismissRequest() }) {
        Surface(
            modifier = Modifier
                .width(dialogWidth)
                .widthIn(max = 480.dp)
                .wrapContentSize()
                .align(Alignment.Center)
                .clickable(enabled = false) {}, shape = dialogShape, color = OnPrimary
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = title,
                    style = Typography.titleLarge
                )
                Text(
                    modifier = Modifier
                        .heightIn(min = 60.dp)
                        .fillMaxWidth(),
                    text = text,
                    style = Typography.bodyMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    dismissButton?.let {
                        SolidButton(
                            text = dismissButton,
                            type = ButtonType.Secondary,
                            onClick = {
                                onDismiss?.invoke()
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    SolidButton(
                        text = confirmButton,
                        type = ButtonType.Primary,
                        onClick = {
                            onConfirm()
                        }
                    )
                }
            }
        }
    }
}

