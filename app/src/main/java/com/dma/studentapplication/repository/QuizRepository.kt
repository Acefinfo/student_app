package com.dma.studentapplication.repository

import android.content.Context
import com.dma.studentapplication.model.Question
import com.dma.studentapplication.model.QuestionList
import kotlinx.serialization.json.Json

class QuizRepository(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    fun loadQuestions(fileName: String): List<Question> {
        val jsonString = context.assets
            .open(fileName)
            .bufferedReader()
            .use { it.readText() }

        val questionList = json.decodeFromString<QuestionList>(jsonString)
        return questionList.questions.shuffled().take(10)
    }
}