package com.muhammadwahyudin.notesmvvm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.notesmvvm.data.model.Note
import kotlinx.android.synthetic.main.note_item.view.*

// ClickListener function must provided on constructor
// Using ListAdapter for builtin DiffUtil (insert/delete Animation related)
class NoteAdapter(private val clickListener: (Note) -> Unit) : ListAdapter<Note, NoteAdapter.NoteHolder>(diffCallback) {

    companion object {
        var diffCallback: DiffUtil.ItemCallback<Note> = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.title == newItem.title &&
                        oldItem.description == newItem.description &&
                        oldItem.priority == newItem.priority
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteHolder(mView)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    fun getNoteAt(position: Int): Note {
        return getItem(position)
    }

    /**
     * NoteHolder
     */
    inner class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note, clickListener: (Note) -> Unit) {
            itemView.tv_note_item_title.text = note.title
            itemView.tv_note_item_desc.text = note.description
            itemView.tv_note_item_priority.text = note.priority.toString()
            itemView.setOnClickListener { clickListener(note) } // Set click listener
        }
    }

}