package com.dma.studentapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dma.studentapplication.ui.HomeScreen
import com.dma.studentapplication.ui.QuizScreen
import com.dma.studentapplication.ui.SummaryScreen
import com.dma.studentapplication.ui.theme.StudentApplicationTheme
import com.dma.studentapplication.viewmodel.QuizViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentApplicationTheme {
                StudentApplication()
            }
        }
    }
}

@Composable
fun StudentApplication(){
    val navController = rememberNavController()
    val viewModel: QuizViewModel= viewModel()
    AppNavHost(navController = navController, viewModel = viewModel)
}

@Composable
fun AppNavHost(navController: NavHostController, viewModel: QuizViewModel){
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController, viewModel)
        }
        composable("quiz") {
            QuizScreen(navController, viewModel)
        }
        composable("summary") {
            SummaryScreen(navController, viewModel)
        }
    }
}