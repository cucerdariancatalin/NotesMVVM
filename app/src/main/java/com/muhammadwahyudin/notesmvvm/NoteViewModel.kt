package com.muhammadwahyudin.notesmvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.notesmvvm.data.AppRepository
import com.muhammadwahyudin.notesmvvm.data.model.Note

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    var repository: AppRepository = AppRepository(application)
    var allNotes: LiveData<List<Note>> = repository.allNotes

    fun insert(note: Note) {
        repository.insertNote(note)
    }
    fun update(note: Note) {
        repository.updateNote(note)
    }
    fun delete(note: Note) {
        repository.deleteNote(note)
    }
    fun deleteAllNotes() {
        repository.deleteAllNotes()
    }
}