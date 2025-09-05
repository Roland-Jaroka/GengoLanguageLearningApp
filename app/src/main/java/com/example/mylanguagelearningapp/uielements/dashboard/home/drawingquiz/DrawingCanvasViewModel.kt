package com.example.mylanguagelearningapp.uielements.dashboard.home.drawingquiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.mylanguagelearningapp.words.JapaneseWords
import com.example.mylanguagelearningapp.model.QuizManager
import com.example.mylanguagelearningapp.model.UserSettingsRepository
import com.example.mylanguagelearningapp.model.Words
import com.example.mylanguagelearningapp.words.ChineseWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DrawingState(
    val color: Color = Color.Black,
    val currentPath: PathData? = null,
    val paths: List<PathData> = emptyList()
)

data class PathData(
    val id: String,
    val color: Color = Color.Black,
    val path: MutableList<Offset> = mutableListOf()
)

sealed interface DrawingActions{
    data object OnNewPathStart: DrawingActions
    data class OnDraw(val offset: Offset): DrawingActions
    data object OnPathEnd: DrawingActions
    data object OnClearCanvas: DrawingActions
}
class DrawingCanvasViewModel: ViewModel(){
 private val _state= MutableStateFlow(DrawingState())
    val state= _state.asStateFlow()

    fun onAction(action: DrawingActions){

        when(action){
            is DrawingActions.OnDraw -> onDraw(action.offset)
            DrawingActions.OnNewPathStart -> onNewPathStart()
            DrawingActions.OnClearCanvas -> onClearCanvas()
            DrawingActions.OnPathEnd -> onPathEnd()
        }
    }

    private fun onPathEnd() {
        val currentPathData= _state.value.currentPath?: return
        _state.update { it.copy(
            currentPath = null,
            paths = it.paths + currentPathData
        ) }
    }

    private fun onClearCanvas() {
        _state.value= _state.value.copy(
            currentPath = null,
            paths = emptyList()
        )
    }

    private fun onNewPathStart() {
        _state.update { it.copy(
            currentPath = PathData(
                id = System.currentTimeMillis().toString(),
                color = it.color,
                path = mutableListOf()
            )
        ) }
    }

    private fun onDraw(offset: Offset) {
        val currentPathData= _state.value.currentPath?: return
        val updatedPath= currentPathData.path.toMutableList().apply{
            add(offset)
        }
        val updatedPathData= currentPathData.copy(path = updatedPath)

        _state.update { it.copy(
            currentPath = updatedPathData
        ) }
    }

    //Quiz logic
    val currentLanguage= UserSettingsRepository.language.value
    val wordsList = if (QuizManager.quizzes.isNotEmpty()) QuizManager.quizzes
    else if (currentLanguage=="jp") JapaneseWords.wordList
    else if (currentLanguage=="cn") ChineseWords.chinseWordsList
    else JapaneseWords.wordList

    var currentIndex by mutableStateOf(0)
        private set
    var currentWord = mutableStateOf<Words?>(wordsList[0])


    var isKanjiReveled by mutableStateOf(false)


    fun onNextClick() {

        if (wordsList.isEmpty()) return

        currentIndex = (currentIndex + 1) % wordsList.size


        currentWord.value = wordsList[currentIndex]



    }

    fun onBackClick() {
        if (wordsList.isEmpty()) return

        currentIndex = (currentIndex - 1 + wordsList.size) % wordsList.size

        currentWord.value = wordsList[currentIndex]




    }

}