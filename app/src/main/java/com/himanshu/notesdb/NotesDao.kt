package com.himanshu.notesdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NotesDao {
    @Insert
    fun insertNotes(notesEntity: NotesEntity)

    @Query("SELECT * FROM NotesEntity")
    fun getNotes(): List<NotesEntity>

    @Delete
    fun deleteNotes(notesEntity: NotesEntity)

    @Update
    fun updateNotes(notesEntity: NotesEntity)

}