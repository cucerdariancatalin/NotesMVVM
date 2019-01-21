package com.muhammadwahyudin.notesmvvm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.notesmvvm.data.model.Note
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    companion object {
        // Activity Result Identifier
        var ADD_NOTE_REQUEST = 1
        var EDIT_NOTE_REQUEST = 2
    }

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Instantiate Note's viewmodel
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)

        fab_add_note.setOnClickListener {
            // Add Note
            startActivityForResult<AddEditNoteActivity>(ADD_NOTE_REQUEST)
        }

        // Setup RecyclerView & Adapter
        val adapter = NoteAdapter(fun(note: Note){
            // Specify on click listener
            // Put Edit Request Code & Note's content as intent extras
            startActivityForResult<AddEditNoteActivity>(
                EDIT_NOTE_REQUEST,
                AddEditNoteActivity.EXTRA_ID to note.id,
                AddEditNoteActivity.EXTRA_TITLE to note.title,
                AddEditNoteActivity.EXTRA_DESC to note.description,
                AddEditNoteActivity.EXTRA_PRIORITY to note.priority
            )
        })
        rv_notes.layoutManager = LinearLayoutManager(this)
        rv_notes.adapter = adapter

        // Observe Note's ViewModel for data change
        noteViewModel.allNotes.observe(this, Observer<List<Note>> {
            // Update Adapter if data changed
            adapter.submitList(it)
        })

        // Set RecyclerView Item Swipe Action
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.RIGHT.or(ItemTouchHelper.LEFT)
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Delete swiped (Left/Right) item
                noteViewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                toast("Note deleted")
            }

        }).attachToRecyclerView(rv_notes)

    }

    /**
     *  ACTIVITY MENU
     */

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.main_action_delete_all_notes -> {
                noteViewModel.deleteAllNotes()
                toast("All notes deleted")
                return true
            }
            R.id.main_action_debug_add_notes -> {
                for (i in 1..10) {
                    noteViewModel.insert(Note("Title $i", "Description $i", i))
                }
                toast("Added 10 debug notes")
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    /**
     * ACTIVITY result
     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK -> {
                var title = data?.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)!!
                var desc = data.getStringExtra(AddEditNoteActivity.EXTRA_DESC)!!
                var priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1)

                noteViewModel.insert(Note(title, desc, priority))
                toast("Note saved")

            }
            requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK -> {
                var id = data?.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1)!!

                if (id == -1) {
                    toast("Note can't be updated")
                    return
                }

                var title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)!!
                var desc = data.getStringExtra(AddEditNoteActivity.EXTRA_DESC)!!
                var priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1)

                var note = Note(title, desc, priority)
                note.id = id

                noteViewModel.update(note)
                toast("Note updated")
            }
            else -> {
                toast("Note not saved")
            }
        }
    }
}
