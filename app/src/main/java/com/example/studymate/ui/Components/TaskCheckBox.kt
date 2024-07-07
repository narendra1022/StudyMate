package com.example.studymate.ui.Components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TaskCheckBox(
    modifier: Modifier = Modifier,
    isComplete: Boolean,
    boarderColor: Color,
    onBoxClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(25.dp)
            .border(2.dp, boarderColor, CircleShape)
            .clickable { onBoxClick },
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        AnimatedVisibility(visible = isComplete) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Rounded.Check,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun taskcheckbox(modifier: Modifier = Modifier) {
//    TaskCheckBox()
}