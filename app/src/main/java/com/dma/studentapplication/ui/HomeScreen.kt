package com.dma.studentapplication.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dma.studentapplication.ui.theme.Indigo
import com.dma.studentapplication.ui.theme.StudentApplicationTheme
import com.dma.studentapplication.ui.theme.TextMuted
import com.dma.studentapplication.ui.theme.TextPrimary
import com.dma.studentapplication.viewmodel.QuizViewModel
import com.dma.studentapplication.ui.theme.Surface as AppSurface


// Map of topic names (shown to user) → JSON file names (This is only used internally)
val topics = mapOf(
    "Activity & Intent" to "activity_&_intent.json",
    "Jetpack Compose"   to "jetpack_compose.json",
    "Kotlin"            to "kotlin.json",
    "ViewModel & Room"  to "viewmodel_&_room.json"
)


// Home page ui
@Composable
fun HomeScreen(navController: NavController, viewModel: QuizViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Small label text
        Text(
            text = "ANDROID QUIZ",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Choose a topic",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Scrollable list of topics
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(topics.entries.toList()) { (topicName, fileName) ->
                TopicCard(
                    topicName = topicName,
                    onClick = {
                        viewModel.loadQuestions(fileName)
                        navController.navigate("quiz") // Navigate to quiz screen
                    }
                )
            }
        }
    }
}

// Reusable UI component for each topic
@Composable
fun TopicCard(topicName: String, onClick: () -> Unit) {
    // Clickable card surface
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = AppSurface,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Horizontal layout inside card
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween, // Space between text and arrow
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Topic name text
            Text(
                text = topicName,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            // Arrow icon (just a text symbol)
            Text(
                text = "›",
                style = MaterialTheme.typography.titleLarge,
                color = Indigo
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun HomeScreenPreview() {
    StudentApplicationTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "ANDROID QUIZ",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Choose a topic",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(32.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(topics.entries.toList()) { (topicName, _) ->
                    TopicCard(topicName = topicName, onClick = {})
                }
            }
        }
    }
}