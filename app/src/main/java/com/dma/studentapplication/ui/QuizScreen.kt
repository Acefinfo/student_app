package com.dma.studentapplication.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dma.studentapplication.ui.theme.Indigo
import com.dma.studentapplication.ui.theme.StudentApplicationTheme
import com.dma.studentapplication.ui.theme.SurfaceBorder
import com.dma.studentapplication.ui.theme.TextMuted
import com.dma.studentapplication.ui.theme.TextPrimary
import com.dma.studentapplication.viewmodel.QuizViewModel
import com.dma.studentapplication.ui.theme.Surface as AppSurface

@Composable
fun QuizScreen(navController: NavController, viewModel: QuizViewModel) {

    // Collecting data from the ViewModel
    val currentIndex by viewModel.currentIndex.collectAsState()
    val isFinished by viewModel.isFinished.collectAsState()
    val selectedAnswer by viewModel.selectedAnswer.collectAsState()
    val total = viewModel.getTotalQuestions()
    val question = viewModel.getCurrentQuestion() ?: return
    val labels = listOf("A", "B", "C", "D") // Labels for answer options

    LaunchedEffect(isFinished) {
        if (isFinished) {  // Navigate to summary screen
            navController.navigate("summary") {
                popUpTo("quiz") { inclusive = true }
            }
        }
    }

    // Vertical layout container for the quiz screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Row for displaying topic and question count
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = viewModel.currentTopic.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
            Text(
                text = "${currentIndex + 1} / $total",
                style = MaterialTheme.typography.labelLarge,
                color = Indigo
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Segmented progress bar for question navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(total) { i ->
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp),
                    shape = RoundedCornerShape(4.dp),
                    color = if (i <= currentIndex) Indigo else SurfaceBorder
                ) {}
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Question text
        Text(
            text = question.questionText,
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Display answer options (A, B, C, D)
        question.options.forEachIndexed { index, option ->
            val isSelected = selectedAnswer == index
            Surface(
                onClick = { viewModel.selectAnswer(index) },
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) Indigo else AppSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                Text(
                    text = "${labels[index]}. $option", // Option label (A, B, C, D) followed by the option text
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected) TextPrimary else TextMuted,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Prev/Next buttons row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Previous button (disabled if on the first question)
            Surface(
                onClick = { if (currentIndex > 0) viewModel.previousQuestion() },
                shape = RoundedCornerShape(12.dp),
                color = if (currentIndex > 0) AppSurface else SurfaceBorder,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "<- Prev",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (currentIndex > 0) TextMuted else TextMuted.copy(alpha = 0.3f),
                    modifier = Modifier
                        .padding(vertical = 14.dp)
                        .fillMaxWidth()
                        .then(Modifier.padding(horizontal = 16.dp))
                )
            }

            // Next/Finish button (enabled if an option is selected)
            Surface(
                onClick = { if (selectedAnswer != null) viewModel.submitAnswer() },
                shape = RoundedCornerShape(12.dp),
                color = if (selectedAnswer != null) Indigo else SurfaceBorder,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (currentIndex == total - 1) "Finish" else "Next ->",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (selectedAnswer != null) TextPrimary else TextMuted.copy(alpha = 0.3f),
                    modifier = Modifier
                        .padding(vertical = 14.dp)
                        .fillMaxWidth()
                        .then(Modifier.padding(horizontal = 16.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// Preview of the QuizScreen UI
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun QuizScreenPreview() {
    StudentApplicationTheme {
        val labels = listOf("A", "B", "C", "D")
        val sampleOptions = listOf(
            "val is immutable, var is mutable",
            "val is a variable, var is a value",
            "No difference",
            "val is for numbers only"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "KOTLIN",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
                Text(
                    text = "6 / 10",
                    style = MaterialTheme.typography.labelLarge,
                    color = Indigo
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(10) { i ->
                    Surface(
                        modifier = Modifier.weight(1f).height(6.dp),
                        shape = RoundedCornerShape(4.dp),
                        color = if (i < 6) Indigo else SurfaceBorder
                    ) {}
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "What is the difference between val and var in Kotlin?",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(24.dp))

            sampleOptions.forEachIndexed { index, option ->
                Surface(
                    onClick = {},
                    shape = RoundedCornerShape(12.dp),
                    color = if (index == 0) Indigo else AppSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                ) {
                    Text(
                        text = "${labels[index]}. $option",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (index == 0) TextPrimary else TextMuted,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    onClick = {},
                    shape = RoundedCornerShape(12.dp),
                    color = AppSurface,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "← Prev",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextMuted,
                        modifier = Modifier
                            .padding(vertical = 14.dp)
                            .fillMaxWidth()
                            .then(Modifier.padding(horizontal = 16.dp))
                    )
                }
                Surface(
                    onClick = {},
                    shape = RoundedCornerShape(12.dp),
                    color = Indigo,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Next →",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextPrimary,
                        modifier = Modifier
                            .padding(vertical = 14.dp)
                            .fillMaxWidth()
                            .then(Modifier.padding(horizontal = 16.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}