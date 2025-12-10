package com.example.gengolearning.uielements.dashboard.home.drawingquiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.model.QuizManager.quizzes
import com.example.gengolearning.model.UserSettingsRepository
import com.example.gengolearning.model.Words
import com.example.gengolearning.words.LanguageWords
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
@HiltViewModel
class DrawingCanvasViewModel @Inject constructor(
    repository: LanguageWords,
    userSettingsRepository: UserSettingsRepository
): ViewModel(){
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
    val currentLanguage= userSettingsRepository.language.value

    private val _currentList = MutableStateFlow<List<Words>>(emptyList())
    val currentList = _currentList.asStateFlow()


    var currentIndex by mutableIntStateOf(0)
        private set
    var currentWord = mutableStateOf<Words?>(null)


    init {
            if (quizzes.isNotEmpty()) {
                currentIndex = 0
                _currentList.value = quizzes
                currentWord.value = currentList.value[0]

            } else {
                repository.words.onEach { list ->
                    if (list.isNotEmpty()) {
                        currentIndex = 0
                        currentWord.value = list[0]
                        _currentList.value = list

                    }

                }.launchIn(viewModelScope)
            }

    }




    var isKanjiReveled by mutableStateOf(false)


    fun onNextClick() {

        if (currentList.value.isEmpty()) return


        currentIndex = (currentIndex + 1) % currentList.value.size


        currentWord.value = currentList.value[currentIndex]

        isKanjiReveled = false



    }

    fun onBackClick() {

        if (currentList.value.isEmpty()) return


        currentIndex = (currentIndex - 1 + currentList.value.size) % currentList.value.size

        currentWord.value = currentList.value[currentIndex]




    }

}