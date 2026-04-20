package com.example.gengolearning.data.repositories

sealed class ProfilePictureState {
    object Loading: ProfilePictureState()
    data class Loaded(val image: ByteArray? = null): ProfilePictureState() {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Loaded

            if (!image.contentEquals(other.image)) return false

            return true
        }

        override fun hashCode(): Int {
            return image?.contentHashCode() ?: 0
        }
    }
}