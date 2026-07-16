package com.example.gengolearning.ui.features.dashboard.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.data.repositories.LanguageGrammar
import com.example.gengolearning.data.repositories.LanguageWords
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.model.AppSettingsPreferences
import com.example.gengolearning.model.appmodels.NewsResponse
import com.example.gengolearning.model.appmodels.ProfileImageState
import com.example.gengolearning.model.results.CloudSyncResults
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


sealed class HomeUiEvents {
    object NavigateToQuiz: HomeUiEvents()
    object UnableToSync: HomeUiEvents()
}
@HiltViewModel
class HomeViewModel @Inject constructor(
   val repository: LanguageWords,
   val userSettingsRepository: UserSettingsRepository,
    val grammarRepo: LanguageGrammar
): ViewModel() {
    val fullList = repository.getAllWords()
    val homePageWords = repository.getHomePageWord()

    val wordList = combine(fullList, homePageWords) {
        list, homePageWords ->
        homePageWords.ifEmpty {
            list
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    val username = userSettingsRepository.username

    val currentLanguage= userSettingsRepository.selectedLanguage

    val selectedLanguage: StateFlow<String> = userSettingsRepository.language

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = combine(_uiState, wordList,currentLanguage, username ) {
        state, wordList, currentLanguage, username ->
        state.copy(
            wordList = wordList,
            currentLanguage = currentLanguage,
            userName = username,
            currentWord = if (wordList.isNotEmpty()) wordList[state.currentIndex] else null
        )
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
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
        loadNews()

        wasSyncSuccessful()

    }

    fun HomeActions(action: HomeActions) {
        when (action) {
            HomeActions.OnCloseNewsModal ->  closeNewsModal()
            HomeActions.OnDismissQuizEmptyModal -> dismissQuizIsEmptyModal()
            HomeActions.OnNextClick -> onNextClick()
            HomeActions.OnOpenNewsModal -> openNewsModal()
            HomeActions.OnPreviousClick -> onPreviousClick()
            HomeActions.OnPronounciationClick -> onPronunciationClick()
            HomeActions.OnQuizClick -> onQuizClick()
            HomeActions.OnSyncWithCloud -> syncroniseWithCloud()
            HomeActions.OnTranslationClick -> onTranslationClick()
            HomeActions.OnWordClick -> onWordClick()
            HomeActions.OnGetData ->  getData()
            HomeActions.OnShowSyncedModal -> showSyncedModal()
        }
    }

    private fun loadNews() {

        viewModelScope.launch {
            val list = mutableListOf<NewsResponse>()
            try {
                val response = repository.getNews()
                response.forEach { item->
                    list.add(item)
                }
                _uiState.update {
                    it.copy(
                        news = list
                    )
                }
            }
            catch(e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun onNextClick() {
        val list = uiState.value.wordList

        if (list.isEmpty()) return

        _uiState.update {
            it.copy(
                currentIndex = (it.currentIndex + 1) % uiState.value.wordList.size,
            )
        }
    }

  private  fun onPreviousClick() {

      val list = uiState.value.wordList

      if (list.isEmpty()) return

      _uiState.update {
          val index = (it.currentIndex - 1) % list.size
          it.copy(

              currentIndex = if (index < 0) list.size - 1 else index
          )
      }
    }

    private fun onWordClick() {
        _uiState.update {
            it.copy(
                isWordVisible = !it.isWordVisible
            )
        }
    }

   private fun onPronunciationClick() {
        _uiState.update {
            it.copy(
                isPronunciationVisible = !it.isPronunciationVisible
            )
        }
    }

   private fun onTranslationClick() {

        _uiState.update {
            it.copy(
                isTranslationVisible = !it.isTranslationVisible
            )
        }
    }

    fun setWelcomeTutorial(context: Context){
        viewModelScope.launch {
            AppSettingsPreferences.setWelcomeTutorialShown(context, false)
        }
    }

   private fun openNewsModal() {
        _uiState.update {
            it.copy(
                newsModal = true
            )
        }
    }

   private fun closeNewsModal() {
        _uiState.update {
            it.copy(
                newsModal = false
            )
        }
    }



  private  fun syncroniseWithCloud(){


        viewModelScope.launch {
            val wordsList = uiState.value.wordList
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

 private   fun onQuizClick() {
        val words = uiState.value.wordList

        if (words.isEmpty()) {
            _uiState.update {
                it.copy(
                    quizIsEmptyModal = true
                )
            }
        } else {
            viewModelScope.launch {
                _uiEvents.emit(HomeUiEvents.NavigateToQuiz)
            }
        }
    }

  private  fun dismissQuizIsEmptyModal() {
       _uiState.update {
           it.copy(
               quizIsEmptyModal = false
           )
       }
    }

    fun testApi() {
        _uiState.update {
            it.copy(
                isLoading = true
            )
        }

            repository.getFirebaseToken { token ->
                viewModelScope.launch {
                    repository.testAPI(token)

                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
            }

    }

    private fun wasSyncSuccessful() {
        viewModelScope.launch {
            repository.cloudSyncWasSuccess.collect { syncResults ->

                if (syncResults is CloudSyncResults.Failure) {
                    _uiEvents.emit(HomeUiEvents.UnableToSync)
                } else {
                    _uiState.update {
                        it.copy(
                            synchronized = true
                        )
                    }
                }
            }
        }
    }

    private fun getData() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSyncing = true
                )
            }
            repository.loadWords(userSettingsRepository.language.first())
            repository.loadCategories(userSettingsRepository.language.first())
            grammarRepo.loadGrammar(userSettingsRepository.language.first())

            _uiState.update {
                it.copy(
                    isSyncing = false
                )
            }
        }
    }

    private fun showSyncedModal() {
        _uiState.update {
            it.copy(
                syncedInfoModal = !it.syncedInfoModal
            )
        }
    }


}

