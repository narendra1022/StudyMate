package com.example.studymate.ui.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.studymate.R
import com.example.studymate.ui.Models.Session
import com.example.studymate.ui.Util.changeMillisToDateString
import com.example.studymate.ui.Util.toHours


fun LazyListScope.studySessionsList(
    sectionTitle: String,
    emptySessionText: String,
    sessions: List<Session>,
    onDeleteIcon: (Session) -> Unit
) {
    item {
        Text(
            text = sectionTitle,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
        )
    }
    if (sessions.isEmpty()) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
//                Image(
//                    modifier = Modifier
//                        .size(100.dp)
//                        .padding(top = 10.dp),
//                    painter = painterResource(id = R.drawable.lamp),
//                    contentDescription = "Lamp Image"
//                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = emptySessionText,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    } else {
        items(sessions) { session ->
            SessionCard(
                modifier = Modifier.padding(horizontal = 5.dp),
                session = session,
                onDeleteIcon = { onDeleteIcon(session) })
        }
    }
}

@Composable
fun SessionCard(
    modifier: Modifier = Modifier,
    session: Session,
    onDeleteIcon: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {

                Text(
                    text = session.relatedToSubject,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    minLines = 1,
                    maxLines = 1,
                )
                Text(
                    text = session.date.changeMillisToDateString(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = session.duration.toHours().toString(),
                style = MaterialTheme.typography.titleMedium
            )

            IconButton(onClick = onDeleteIcon) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Session")
            }


        }

    }
}
