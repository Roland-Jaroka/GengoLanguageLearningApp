package com.example.gengolearning.model.appmodels

sealed class ProfileImageState {
    object Loading: ProfileImageState()
    object Empty: ProfileImageState()
    data class LoadedImage(val image: ByteArray): ProfileImageState() {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as LoadedImage

            if (!image.contentEquals(other.image)) return false

            return true
        }

        override fun hashCode(): Int {
            return image.contentHashCode()
        }
    }
}


