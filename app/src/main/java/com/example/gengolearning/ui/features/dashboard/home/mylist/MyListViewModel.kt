package com.example.gengolearning.ui.features.dashboard.home.mylist

import android.content.Context
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gengolearning.data.repositories.LanguageWords
import com.example.gengolearning.data.repositories.UserSettingsRepository
import com.example.gengolearning.model.AppSettingsPreferences
import com.example.gengolearning.model.appmodels.WordCategories
import com.example.gengolearning.model.appmodels.Words
import com.example.gengolearning.model.utils.QuizManager.quizzes
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class MyListViewModel @Inject constructor(
    private val repository: LanguageWords,
     userSettingsRepository: UserSettingsRepository
): ViewModel() {

    val currentLanguage= userSettingsRepository.language

   val words = repository.words.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    private val categories = repository.categories.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    private val _searchInput = MutableStateFlow("")
    private val _state = MutableStateFlow(MyListUiState())
    val state = combine(_state, words, categories, _searchInput) {
            state, words, categories, searchInput ->
          val filteredWords = if (searchInput.isBlank()) {
              words
          } else {
              words.filter {word -> listOf(word.word, word.translation, word.pronunciation).any {
                  it.contains(searchInput, ignoreCase = true)
              }
              }
          }
        state.copy(
            words = filteredWords,
            categories = categories,
            searchInput = searchInput
        )
    }.stateIn(viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MyListUiState())

     private val _uiEvents = MutableSharedFlow<MyListUiEvents>()
    val uiEvents = _uiEvents.asSharedFlow()

    fun myListActions(action: MyListActions) {
        when (action) {
            is MyListActions.OnAddCategoryToSelectedWords -> addCategoryToSelectedWords(action.category)
            is MyListActions.OnCategoryDeleteButton -> onCategoryDeleteButton(action.category)
            is MyListActions.OnCategoryLongTap -> {
                onCategoryLongTap(action.category)
            }
            is MyListActions.OnDeleteCategory -> onDeleteCategory(action.category)
            MyListActions.OnEdit -> onEdit()
            MyListActions.OnEditWord -> onEditWord()
            MyListActions.OnHomeCard -> onHomeCard()
            MyListActions.OnHomePage -> onHomepage()
            is MyListActions.OnInputChanged -> onInputChanged(action.newInput)
            is MyListActions.OnLongTap -> {
                onLongTap(action.word)
            }
            MyListActions.OnRemove -> onRemove()
            MyListActions.OnSelectAll -> selectAll()
            MyListActions.OnSendWordsToDrawingQuiz -> onSendWordsToDrawingQuiz()
            MyListActions.OnSendWordsToQuiz -> onSendWordsToQuiz()
            is MyListActions.OnToggleCategorySelection -> toggleCategorySelection(action.categoryName, action.wordId)
            is MyListActions.OnToggleSelection -> toggleSelection(action.id)
            MyListActions.OnDeleteCategoryButton -> showDeleteDialog()
            MyListActions.OnAddCategoryButton -> showCategoryBottomSheet()
            MyListActions.OnListViewChange -> setCategoryListView()
            MyListActions.OnDismissWordLongTapModal -> dismissWordLongTapModal()
            MyListActions.OnDismissCategoryLongTapModal -> dismissCategoryLongTapModal()
            MyListActions.OnDismissNewCategoryModal -> dismissNewCategoryModal()
            MyListActions.OnHideDeleteWordDialog -> hideDeleteWordDialog()
            MyListActions.OnDismissCategoryDeleteDialog -> dismissCategoryDeleteDialog()
            MyListActions.OnDismissCategoryBottomSheet -> dismissCategoryBottomSheet()
            MyListActions.OnEditCategory -> onEditCategory()
            MyListActions.OnDismissQuizIsEmptyModal -> onDismissQuizIsemptyModal()
            MyListActions.OnPronounciationCopy -> {
                val longTappedWord = state.value.longTappedWord
                onCopy(longTappedWord?.pronunciation ?: "", CopyOperations.COPY_PRONOUNCIATION)
            }
           MyListActions.OnTranslationCopy -> {
                val longTappedWord = state.value.longTappedWord
                onCopy(longTappedWord?.translation ?: "", CopyOperations.COPY_TRANSLATION)
            }
           MyListActions.OnWordCopy -> {
                val longTappedWord = state.value.longTappedWord
                onCopy(longTappedWord?.word ?: "", CopyOperations.COPY_WORD)
            }
        }
    }
   private fun onInputChanged(newInput: String) {
        _searchInput.value = newInput
    }

    private fun showDeleteDialog(){
        val selectedWords = _state.value.selectedWords

       if (selectedWords.isNotEmpty()) _state.update {
           it.copy(
               showDeleteDialog = true
           )
       }
    }

 private   fun  hideDeleteWordDialog(){
        _state.update {
            it.copy(
                showDeleteDialog = false
            )
        }
    }

   private fun onEdit() {
        _state.update {
            it.copy(
                onEdit = !it.onEdit
            )
        }
    }


    fun tutorialModal(context: Context) = AppSettingsPreferences.showMyListTutorial(context)

    private fun toggleSelection(id: String) {

        val selectedWords = _state.value.selectedWords

        if (selectedWords.contains(id)) {
            _state.update {
                it.copy(
                    selectedWords = selectedWords - id
                )
            }
        } else {

            _state.update {
                it.copy(
                    selectedWords = selectedWords + id
                )
            }
        }
    }

    private fun toggleCategorySelection(categoryName: String, wordId: List <Words>) {

           val selectedCategories = _state.value.selectedCategories

        if (selectedCategories.contains(categoryName)) {
            _state.update {
                it.copy(
                    selectedCategories = selectedCategories - categoryName
                )
            }
        } else {
            _state.update {
                it.copy(
                    selectedCategories = selectedCategories + categoryName
                )
            }
        }

        wordId.forEach { word ->
            toggleSelection(word.id)
        }


    }

    private fun selectAll(){
          val selectedWords = state.value.selectedWords

        if (selectedWords.size != words.value.size) {

            _state.update {
                it.copy(
                    selectedWords = words.value.map {word-> word.id }
                )
            }

        } else {
            _state.update {
                it.copy(
                    selectedWords = emptyList()
                )
            }
        }
    }


 private   fun onRemove(){
         val selectedWords = _state.value.selectedWords
        viewModelScope.launch {
            selectedWords.forEach { id ->
                repository.onRemove(id, currentLanguage.value)
            }
            _state.update {
                it.copy(
                    showDeleteDialog = false,
                    selectedWords = emptyList()
                )
            }

        }
    }

  private  fun onDeleteCategory(category: WordCategories) {

        val wordsInCategory = words.value.filter { it.category.contains(category.categoryName) }

        viewModelScope.launch {

            wordsInCategory.forEach { word->
                repository.updateLocalWord(
                    word.copy(category = word.category - category.categoryName)
                ) //Local

                repository.updateWordWithCategoryOnFirebase(
                    word.copy(category = word.category - category.categoryName),
                    language = currentLanguage.value
                ) //Firebase
            }


            repository.deleteCategory(category)//Local

            repository.removeCategoryFromFirebase(category.id,
                currentLanguage.value) //Firebase


            dismissCategoryDeleteDialog()


        }
    }

  private  fun onSendWordsToDrawingQuiz(){
        val words= words.value
        val selectedWords = _state.value.selectedWords
        quizzes.clear()
        selectedWords.forEach { id ->
            words.find { it.id == id }?.let { quizzes.add(it)
                println("Word added to drawing quiz: $quizzes")
            }
        }

    }

  private  fun onSendWordsToQuiz(){
        val words= words.value
        val selectedWords = _state.value.selectedWords
        println("Words: $words")

      if (words.isEmpty() && quizzes.isEmpty()) {
          _state.update {
              it.copy(
                  quizIsEmptyModal = true
              )
          }
      } else {
          quizzes.clear()
          selectedWords.forEach { id ->
              words.find { it.id == id }?.let { word ->
                  quizzes.add(word)
                  println("Word added to quiz: $quizzes")

              }
          }
          viewModelScope.launch {
              _uiEvents.emit(MyListUiEvents.NavigateToQuiz)
          }
      }
    }



  private  fun onHomepage(){

        val selectedWords = _state.value.selectedWords

        if (selectedWords.isEmpty()) return


          viewModelScope.launch {

              val allWords = words.value
              allWords.filter { it.id in selectedWords && it.isOnHomePage == false || it.isOnHomePage == null }
                  .forEach { words ->
                      repository.onHomePage(words.id,  true)
                      println("Word added to home page: $words")
                  }
              allWords.filter { it.id !in selectedWords && it.isOnHomePage == true }
                  .forEach { words ->
                      repository.onHomePage(words.id,  false)
                      println("Word removed from home page: $words")
                  }

              _state.update {
                  it.copy(
                      selectedWords = emptyList()
                  )
              }
          }

    }

  private  fun onHomeCard(){

        _state.update {
            it.copy(
                selectedWords = emptyList()
            )
        }

        val homePageWordsIdList = mutableListOf<String>()

        val homePageWords = words.value.filter {it.isOnHomePage == true}
            if(homePageWords.isNotEmpty()){
                homePageWords.forEach {
                    homePageWordsIdList.add(it.id)
                }

                _state.update {
                    it.copy(
                        selectedWords = homePageWordsIdList
                    )
                }
            }
            //At first when no word is selected this will select all the words cause all of them is
            //visible on the home screen
            else {
                _state.update {
                    it.copy(
                        selectedWords = words.value.map { word-> word.id }
                    )
                }
            }


    }

    fun setMyListTutorial(context: Context){
        viewModelScope.launch {
            AppSettingsPreferences.setMyListTutorialShown(context, false)
        }
    }

   private fun onLongTap(word: Words) {
        _state.update {
            it.copy(
                longTap = true,
                longTappedWord = word
            )
        }
    }

   private fun onCategoryLongTap(category: WordCategories) {
        _state.update {
            it.copy(
                categoryLongTap = true,
                longTappedCategory = category
            )
        }
    }

   private fun addCategoryToSelectedWords(category: String) {

        val selectedWords = _state.value.selectedWords

        if (selectedWords.isEmpty()) return


        val words = words.value

        viewModelScope.launch {



            selectedWords.forEach { id ->
                words.find { it.id == id }?.let {

                    if (it.category.contains(category)) return@let

                        repository.updateWordWithCategory(
                            it.copy(category = it.category + category)
                        )

                       repository.updateWordWithCategoryOnFirebase(
                           it.copy(
                               category = it.category + category
                           ),
                           language = currentLanguage.value
                       )


                }
            }

           _state.update {
               it.copy(
                   selectedWords = emptyList(),
                   showCategoryBottomSheet = false
               )
           }

        }

    }


  private  fun dismissWordLongTapModal() {
        _state.update {
            it.copy(
                longTap = false
            )
        }
    }

  private  fun dismissCategoryLongTapModal() {
        _state.update {
            it.copy(
                categoryLongTap = false
            )
        }
    }

  private  fun dismissNewCategoryModal() {
        _state.update {
            it.copy(
                newCategoryModal = false
            )
        }
    }

  private  fun showCategoryBottomSheet() {
        _state.update {
            it.copy(
                showCategoryBottomSheet = true
            )
        }
    }

  private  fun dismissCategoryBottomSheet() {
        _state.update {
            it.copy(
                showCategoryBottomSheet = false
            )
        }
    }

   private fun setCategoryListView(){
        _state.update {
            it.copy(
                categoryListView = !it.categoryListView,
                selectedWords = emptyList()
            )
        }

    }


   private fun dismissCategoryDeleteDialog() {
    _state.update {
        it.copy(
            showCategoryDeleteDialog = false
        )
    }
}

   private fun onEditWord() {

        dismissWordLongTapModal()

        viewModelScope.launch {
            _uiEvents.emit(MyListUiEvents.NavigateToAddWords)
        }
    }

    private fun onCategoryDeleteButton(category: WordCategories) {
        _state.update {
            it.copy(
                categoryToDelete = category,
                showCategoryDeleteDialog = true
            )
        }
    }

    private fun onEditCategory(){
        dismissCategoryLongTapModal()
        viewModelScope.launch {
           _uiEvents.emit(MyListUiEvents.NavigateToNewCategory)
        }
    }

    private fun onDismissQuizIsemptyModal() {
        _state.update {
            it.copy(
                quizIsEmptyModal = false
            )
        }
    }

    private fun onCopy(word: String, copyOperations: CopyOperations) {

        viewModelScope.launch {
            _uiEvents.emit(MyListUiEvents.CopyToClipboard(word, copyOperations))
        }
    }





}