package com.example.gengolearning.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.gengolearning.model.appmodels.ProfilePicture
import kotlinx.coroutines.flow.Flow
@Dao
interface ProfileDao {
    @Query("SELECT * FROM profilepicture WHERE id = :id")
     fun getProfilePicture(id: Int): Flow<ProfilePicture?>

    @Upsert
    suspend fun upsertProfilePicture(profilePicture: ProfilePicture)

    @Delete
    suspend fun deleteProfilePicture(profilePicture: ProfilePicture)



}