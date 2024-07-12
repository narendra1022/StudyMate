package com.example.studymate.ui.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.studymate.R
import com.example.studymate.ui.Models.Task
import com.example.studymate.ui.Util.Priority
import com.example.studymate.ui.Util.changeMillisToDateString

fun LazyListScope.tasksList(
    sectionTitle: String,
    tasks: List<Task>,
    emptyTaskText: String,
    onTaskCardClick: (Int?) -> Unit,
    onCheckBoxClick: (Task) -> Unit
) {
    item {
        Text(
            text = sectionTitle,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(12.dp)
        )
    }
    if (tasks.isEmpty()) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(25.dp))
                Image(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(top = 10.dp),
                    painter = painterResource(id = R.drawable.taksks),
                    contentDescription = "Tasks Image"
                )
                Spacer(modifier = Modifier.height(25.dp))
                Text(
                    text = emptyTaskText,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(25.dp))
            }
        }
    } else {
        items(tasks) { task ->
            TaskCard(
                modifier = Modifier.fillMaxWidth(),
                task = task,
                onBoxClick = { onCheckBoxClick(task) },
                onClick = { onTaskCardClick(task.taskId) })

        }
    }
}

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    task: Task,
    onBoxClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 15.dp, vertical = 10.dp),
    ) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 15.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.width(5.dp))

            TaskCheckBox(
                isComplete = task.isComplete,
                boarderColor = Priority.fromInt(task.priority).color,
                onBoxClick = onBoxClick
            )

            Spacer(modifier = Modifier.width(15.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {

                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    minLines = 1,
                    maxLines = 1,
                    textDecoration = if (task.isComplete) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(
                    text = task.dueDate.changeMillisToDateString(),
                    style = MaterialTheme.typography.bodySmall
                )


            }
        }

    }
}
