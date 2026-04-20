package com.example.gengolearning.model.appmodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gengolearning.app.R

@Entity
data class ProfilePicture(
    @PrimaryKey
    val id: Int= 1,
    val image: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProfilePicture

        if (id != other.id) return false
        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + image.contentHashCode()
        return result
    }
}
