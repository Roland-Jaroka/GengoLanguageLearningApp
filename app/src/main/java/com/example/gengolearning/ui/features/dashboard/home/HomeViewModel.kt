package com.example.gengolearning.ui.features.dashboard.home

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.data.repositories.LanguageWords
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.model.AppSettingsPreferences
import com.example.gengolearning.model.appmodels.NewsResponse
import com.example.gengolearning.model.appmodels.ProfileImageState
import com.example.gengolearning.model.appmodels.Words
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class HomeUiEvents {
    object NavigateToQuiz: HomeUiEvents()
}
@HiltViewModel
class HomeViewModel @Inject constructor(
   val repository: LanguageWords,
   val userSettingsRepository: UserSettingsRepository
): ViewModel() {

    val _wordsList = MutableStateFlow<List<Words>>(emptyList())
    val wordsList = _wordsList.asStateFlow()




    var currentIndex = mutableStateOf(0)


    var currentWord = mutableStateOf<Words?>(null)

    val currentLanguage= userSettingsRepository.selectedLanguage

    val selectedLanguage: StateFlow<String> = userSettingsRepository.language

    var news = mutableStateListOf<NewsResponse?>()

    var newsModal by mutableStateOf(false)
        private set

    var quizIsEmptyModal by mutableStateOf(false)
        private set


    val username = userSettingsRepository.username.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    val image = userSettingsRepository.profileImage
        .map { picture ->
            when {
                picture == null -> ProfileImageState.Empty
                else -> ProfileImageState.LoadedImage(picture.image)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileImageState.Loading
        )

    private val _uiEvents = MutableSharedFlow<HomeUiEvents>()
    val uiEvents = _uiEvents.asSharedFlow()

    fun showTutorial(context: Context) = AppSettingsPreferences.showWelcomeTutorial(context)


    init {
        repository.words.onEach { list->

            //if word is not empty filter words by isHomePage
            if (list.isNotEmpty()){
                val filteredList = list.filter { it.isOnHomePage == true }
                if (filteredList.isNotEmpty()) {
                    currentIndex.value = 0
                    _wordsList.value = filteredList
                    currentWord.value = wordsList.value[0]
                }

                //if filtered words is empty which is equals to there are no isOnHomePage== true
                else {
                    currentIndex.value = 0
                    _wordsList.value = list
                    currentWord.value = wordsList.value[0]
                }


            }
            //if the word list is empty
            else {
                currentIndex.value = 0
                _wordsList.value = emptyList()
                currentWord.value = null
            }

        }.launchIn(viewModelScope)

        loadNews()

    }

    fun loadNews() {

        viewModelScope.launch {

            try {
                val response = repository.getNews()
                news.clear()
                news.addAll(response)
            }
            catch(e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun onNextClick() {
        val list = wordsList.value

        if (list.isEmpty()) return

        currentIndex.value = (currentIndex.value + 1) % list.size

        currentWord.value = list[currentIndex.value]
    }

    fun onPreviousClick() {

        val list = wordsList.value

        if (list.isEmpty()) return
        currentIndex.value = (currentIndex.value - 1 + list.size) % list.size
        currentWord.value = list[currentIndex.value]
    }

    var isWordVisible by mutableStateOf(true)
    var isPronunciationVisible by mutableStateOf(true)
    var isTranslationVisible by mutableStateOf(true)

    fun onWordClick() {
        isWordVisible = !isWordVisible
    }

    fun onPronunciationClick() {
        isPronunciationVisible = !isPronunciationVisible
    }

    fun onTranslationClick() {

        isTranslationVisible = !isTranslationVisible
    }

    fun setWelcomeTutorial(context: Context){
        viewModelScope.launch {
            AppSettingsPreferences.setWelcomeTutorialShown(context, false)
        }
    }

    fun openNewsModal() {
        newsModal = true
    }

    fun closeNewsModal() {
        newsModal = false
    }



    fun syncroniseWithCloud(){


        viewModelScope.launch {
            val wordsList = _wordsList.value
            val categoryList = repository.categories.first()
            val currentLanguage = userSettingsRepository.selectedLanguage.first()


          wordsList.forEach { word ->
              repository.updateWordWithCategoryOnFirebase(
                  word,
                  currentLanguage.code
              )
          }

          categoryList.forEach { category ->
              repository.addCategoryToFirebase(
                  category,
                  currentLanguage.code
              )
          }

            closeNewsModal()

        }

    }

    fun onQuizClick() {
        val words = _wordsList.value

        if (words.isEmpty()) {
            quizIsEmptyModal = true
        } else {
            viewModelScope.launch {
                _uiEvents.emit(HomeUiEvents.NavigateToQuiz)
            }
        }
    }

    fun dismissQuizIsEmptyModal() {
        quizIsEmptyModal = false
    }


}

