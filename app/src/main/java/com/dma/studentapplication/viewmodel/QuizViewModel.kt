package com.dma.studentapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dma.studentapplication.model.Question
import com.dma.studentapplication.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.collections.mapIndexed

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    // Repository used to load quiz questions(From json file)
    private val repository = QuizRepository(application)
    // List of all quiz questions
    private var questions: List<Question> = emptyList()
    // Stores user's answers: key = question index, value = selected answer index
    private val userAnswers = mutableMapOf<Int, Int>()

    // Current topic name (derived from file name)
    var currentTopic: String = ""
        private set

    // Tracks the current question index (StateFlow for reactive UI updates)
    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    // Tracks the user's score
    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    // Indicates whether the quiz is finished
    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished.asStateFlow()

    // Stores currently selected answer (null = nothing selected)
    private val _selectedAnswer = MutableStateFlow<Int?>(null)
    val selectedAnswer: StateFlow<Int?> = _selectedAnswer.asStateFlow()


    // Loads questions from a given Json file
    fun loadQuestions(fileName: String) {
        // Format topic name nicely
        currentTopic = fileName
            .removeSuffix(".json")
            .replace("_", " ")
            .replace("&", "&")

        // Load questions from repository
        questions = repository.loadQuestions(fileName)

        // Reset quiz state
        _currentIndex.value = 0
        _score.value = 0
        _isFinished.value = false
        _selectedAnswer.value = null
        userAnswers.clear()
    }

    // Returns the current question or null if index is invalid
    fun getCurrentQuestion(): Question? = questions.getOrNull(_currentIndex.value)

    // Returns total number of questions
    fun getTotalQuestions(): Int = questions.size

    // Called when user taps an answer option
    fun selectAnswer(index: Int) {
        _selectedAnswer.value = index
    }

    // Called when user taps Next / Finish
    fun submitAnswer() {
        val selected = _selectedAnswer.value ?: return

        // Save the answer for this question
        userAnswers[_currentIndex.value] = selected

        if (_currentIndex.value + 1 >= questions.size) {
            // Last question — calculate final score and finish
            _score.value = userAnswers.entries.count { (i, ans) ->
                questions[i].correctAnswerIndex == ans
            }
            _isFinished.value = true
        } else {
            // Move to next question
            _currentIndex.value++
            // Restore previously selected answer if user is revisiting
            _selectedAnswer.value = userAnswers[_currentIndex.value]
        }
    }

    // Called when user taps Prev
    fun previousQuestion() {
        if (_currentIndex.value > 0) {
            // Save current selection before going back
            _selectedAnswer.value?.let { userAnswers[_currentIndex.value] = it }
            _currentIndex.value--
            // Restore the answer for the previous question
            _selectedAnswer.value = userAnswers[_currentIndex.value]
        }
    }

    fun getAnsweredQuestions(): List<Triple<Question, Int, Boolean>> {
        return questions.mapIndexed { i, q ->
            val chosen = userAnswers.getOrElse(i) { -1 }
            Triple(q, chosen, chosen == q.correctAnswerIndex)
        }
    }
}