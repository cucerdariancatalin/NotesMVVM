package com.muhammadwahyudin.notesmvvm.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.muhammadwahyudin.notesmvvm.data.local.AppDatabase
import com.muhammadwahyudin.notesmvvm.data.local.NoteDao
import com.muhammadwahyudin.notesmvvm.data.model.Note
import org.jetbrains.anko.doAsync

class AppRepository(application: Application) {
    private var noteDao: NoteDao
    var allNotes: LiveData<List<Note>>

    init {
        val database: AppDatabase = AppDatabase.getInstance(application)!!
        noteDao = database.noteDao()
        allNotes = noteDao.getAllNotes()
    }

    fun insertNote(note: Note){
        doAsync {
            noteDao.insert(note)
        }
    }
    fun updateNote(note: Note){
        doAsync {
            noteDao.update(note)
        }
    }
    fun deleteNote(note: Note){
        doAsync {
            noteDao.delete(note)
        }
    }
    fun deleteAllNotes(){
        doAsync {
            noteDao.deleteAllNotes()
        }
    }



}