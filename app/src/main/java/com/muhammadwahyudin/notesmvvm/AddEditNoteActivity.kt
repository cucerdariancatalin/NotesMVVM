package com.muhammadwahyudin.notesmvvm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_add_note.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class AddEditNoteActivity : AppCompatActivity() {

    companion object {
        var EXTRA_ID = "EXTRA_ID"
        var EXTRA_TITLE = "EXTRA_TITLE"
        var EXTRA_DESC = "EXTRA_DESC"
        var EXTRA_PRIORITY = "EXTRA_PRIORITY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        np_priority.minValue = 1
        np_priority.maxValue = 10

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        if(intent.hasExtra(EXTRA_ID)) {
            title = "Edit Note"
            tiet_title.setText(intent.getStringExtra(EXTRA_TITLE))
            tiet_desc.setText(intent.getStringExtra(EXTRA_DESC))
            np_priority.value = intent.getIntExtra(EXTRA_PRIORITY, 1)

        }else
            title = "Add Note"

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_action_save -> {
                saveNote()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    fun saveNote(){
        var title = tiet_title.text.toString()
        var desc = tiet_desc.text.toString()
        var priority = np_priority.value

        if(title.trim().isEmpty() || desc.trim().isEmpty()){
            til_title.error = "Please insert title"
            til_desc.error = "Please insert description"
            toast("Please insert a title and description")
            return
        }

        var data = Intent()
        data.putExtra(EXTRA_TITLE, title)
        data.putExtra(EXTRA_DESC, desc)
        data.putExtra(EXTRA_PRIORITY, priority)

        var id = intent.getIntExtra(EXTRA_ID, -1)
        if (id!=-1)
            data.putExtra(EXTRA_ID, id)

        setResult(Activity.RESULT_OK, data)
        finish()

    }
}
