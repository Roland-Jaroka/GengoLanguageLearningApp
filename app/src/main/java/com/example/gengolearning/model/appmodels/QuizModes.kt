package com.example.gengolearning.model.appmodels

import com.gengolearning.app.R

enum class QuizModes(val displayName: Int) {
     PronounciationQuiz(
         R.string.pronounciationQuiz_displayname
     ),
     TranslationQuiz(
         R.string.translation_quiz_displayname
     ),

    WordQuiz(
        R.string.meaning_word_quiz_displayname
    ),

    CardPlay(
        R.string.card_play
    )


}


