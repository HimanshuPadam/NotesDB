package com.himanshu.notesdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [NotesEntity::class])
abstract class NotesRoomDB: RoomDatabase() {
    abstract fun notesDao(): NotesDao

    companion object {
        //static variable
        var notesRoomDb: NotesRoomDB ?= null

        fun getNotesDatabase(context: Context): NotesRoomDB {
            if (notesRoomDb == null) {
                notesRoomDb = Room.databaseBuilder(context, NotesRoomDB::class.java, context.resources.getString(R.string.app_name))
                    .build()
            }
            return notesRoomDb!!
        }
    }
}