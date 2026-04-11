package com.dma.studentapplication.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dma.studentapplication.model.Question
import androidx.compose.foundation.lazy.items
import com.dma.studentapplication.ui.theme.CorrectGreen
import com.dma.studentapplication.ui.theme.Indigo
import com.dma.studentapplication.ui.theme.StudentApplicationTheme
import com.dma.studentapplication.ui.theme.TextMuted
import com.dma.studentapplication.ui.theme.TextPrimary
import com.dma.studentapplication.ui.theme.WrongRed
import com.dma.studentapplication.viewmodel.QuizViewModel
import com.dma.studentapplication.ui.theme.Surface as SurfaceColor

// The Summary Screen where the user sees their results after completing the quiz
@Composable
fun SummaryScreen(navController: NavController, viewModel: QuizViewModel) {

    val score by viewModel.score.collectAsState()
    val total = viewModel.getTotalQuestions()
    val answeredQuestions = viewModel.getAnsweredQuestions()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Display score in circle
        Surface(
            shape = RoundedCornerShape(50),
            color = SurfaceColor,
            modifier = Modifier.size(96.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "$score",
                    fontSize = 32.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.SemiBold,
                    color = Indigo
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Quiz complete",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "You scored $score out of $total",
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Correct / Incorrect summary row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SurfaceColor,
                modifier = Modifier.weight(1f)
            ) {
                // Correct box
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$score",
                        style = MaterialTheme.typography.headlineMedium,
                        color = CorrectGreen
                    )
                    Text(
                        text = "Correct",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SurfaceColor,
                modifier = Modifier.weight(1f)
            ) {
                // Incorrect box
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${total - score}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = WrongRed
                    )
                    Text(
                        text = "Incorrect",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Question review list
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(answeredQuestions) { (question, chosenIndex, isCorrect) ->
                QuestionResultCard(
                    question = question,
                    chosenIndex = chosenIndex,
                    isCorrect = isCorrect
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Back to topics button
        Surface(
            onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            },
            shape = RoundedCornerShape(12.dp),
            color = Indigo,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Back to topics",
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
                    .wrapContentWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun QuestionResultCard(
    question: Question,
    chosenIndex: Int,
    isCorrect: Boolean
) {
    val borderColor = if (isCorrect) CorrectGreen else WrongRed
    val labels = listOf("A", "B", "C", "D")

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = SurfaceColor,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            // Correct / Wrong badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = question.questionText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isCorrect)
                        CorrectGreen.copy(alpha = 0.15f)
                    else
                        WrongRed.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (isCorrect) "✓" else "✗",
                        color = borderColor,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // User's answer
            Text(
                text = "Your answer: ${labels.getOrElse(chosenIndex) { "?" }}. ${
                    question.options.getOrElse(chosenIndex) { "No answer" }
                }",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isCorrect) CorrectGreen else WrongRed
            )

            // Show correct answer only if wrong
            if (!isCorrect) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Correct: ${labels[question.correctAnswerIndex]}. ${
                        question.options[question.correctAnswerIndex]
                    }",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CorrectGreen
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SummaryScreenPreview() {
    StudentApplicationTheme {
        val sampleQuestions = listOf(
            Question(
                "Which callback is the first to be called when an Activity is created?",
                listOf("onStart()", "onResume()", "onCreate()", "onInit()"), 2
            ),
            Question(
                "Which class is used to launch another activity in Android?",
                listOf("Intent", "Launcher", "Navigator", "Context"), 0
            ),
            Question(
                "How do you pass data between activities using an Intent?",
                listOf(
                    "Using intent.setData()",
                    "Using intent.putExtra()",
                    "Using intent.addFlags()",
                    "Using intent.setBundle()"
                ), 1
            ),
            Question(
                "Which file must all activities be declared in?",
                listOf("build.gradle", "settings.gradle", "AndroidManifest.xml", "MainActivity.kt"),
                2
            ),
            Question(
                "Which flag ensures an activity is not kept in the history stack?",
                listOf(
                    "FLAG_ACTIVITY_NEW_TASK",
                    "FLAG_ACTIVITY_CLEAR_TOP",
                    "FLAG_ACTIVITY_NO_HISTORY",
                    "FLAG_ACTIVITY_SINGLE_TOP"
                ), 2
            ),
            Question(
                "How do you retrieve a String extra from an Intent?",
                listOf(
                    "intent.getString()",
                    "intent.getStringExtra()",
                    "intent.fetchString()",
                    "intent.data.toString()"
                ), 1
            ),
            Question(
                "What is the 'Back Stack' in Android?",
                listOf(
                    "A list of background services",
                    "A set of activities in the order opened",
                    "The undo history",
                    "The list of installed apps"
                ), 1
            ),
            Question(
                "How can you close an activity programmatically?",
                listOf("stop()", "close()", "finish()", "exit()"), 2
            ),
            Question(
                "What does 'onSaveInstanceState()' do?",
                listOf(
                    "Saves data to a permanent database",
                    "Saves temporary UI state before destruction",
                    "Saves the user's password",
                    "Prevents destruction"
                ), 1
            ),
            Question(
                "Which callback is the final call before an activity is destroyed?",
                listOf("onStop()", "onFinish()", "onDestroy()", "onTerminate()"), 2
            )
        )

        // Simulate user answers — questions 1,3,8 wrong, rest correct
        val userAnswers = mapOf(
            0 to 2, 1 to 1, 2 to 1, 3 to 2,
            4 to 2, 5 to 1, 6 to 1, 7 to 2,
            8 to 0, 9 to 2
        )

        val answeredQuestions = sampleQuestions.mapIndexed { i, q ->
            val chosen = userAnswers[i] ?: -1
            Triple(q, chosen, chosen == q.correctAnswerIndex)
        }

        val score = answeredQuestions.count { it.third }
        val total = sampleQuestions.size

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Surface(
                shape = RoundedCornerShape(50),
                color = SurfaceColor,
                modifier = Modifier.size(96.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "$score",
                        fontSize = 32.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.SemiBold,
                        color = Indigo
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Quiz complete",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "You scored $score out of $total",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceColor,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$score",
                            style = MaterialTheme.typography.headlineMedium,
                            color = CorrectGreen
                        )
                        Text(
                            text = "Correct",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceColor,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${total - score}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = WrongRed
                        )
                        Text(
                            text = "Incorrect",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(answeredQuestions) { (question, chosenIndex, isCorrect) ->
                    QuestionResultCard(
                        question = question,
                        chosenIndex = chosenIndex,
                        isCorrect = isCorrect
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                onClick = {},
                shape = RoundedCornerShape(12.dp),
                color = Indigo,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Back to topics",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextPrimary,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth()
                        .wrapContentWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}