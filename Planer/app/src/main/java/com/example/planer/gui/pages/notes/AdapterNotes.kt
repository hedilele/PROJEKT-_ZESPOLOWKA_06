package com.example.planer.gui.pages.home.notes

import android.view.*
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.entities.Notes
import com.example.planer.gui.callBacks.NoteDiffCallback

import kotlinx.android.synthetic.main.single_short_note.view.*

// klasa odpowiedzialna za umieszczanie pojedynczych habitsow w recyclerView
class AdapterNotes(
    var list: MutableList<Notes>,
    private val deleteListener: (Notes) -> Unit,
    private val updateListener: (Notes) -> Unit,
    private val search: (note: String) -> Unit
) : RecyclerView.Adapter<AdapterNotes.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    fun updateList(newNotes: MutableList<Notes>) {
        val diffResult = DiffUtil.calculateDiff(
            NoteDiffCallback(this.list, newNotes)
        )
        this.list = newNotes
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_short_note, parent, false) as CardView
        )

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.itemView.note_title.setText(item.noteContent)

        holder.itemView.setOnClickListener {

            val builder = AlertDialog.Builder(holder.itemView.context) //TODO
            val inflater = LayoutInflater.from(holder.itemView.context)
            val dialogView = inflater.inflate(R.layout.dialog_short_note, null)
            builder.setView(dialogView) //Podlaczanie xmla

            val content = dialogView.findViewById<AppCompatEditText>(R.id.note_content)
            val btn_edit = dialogView.findViewById<Button>(R.id.btn_edit)
            content.setText(item.noteContent)


            val alertDialog = builder.create()
            alertDialog.show()

            btn_edit.setOnClickListener {
                var noteTmp = item
                noteTmp.noteContent = content.text.toString()
                updateListener(noteTmp)
                alertDialog.cancel()
            }
        }

        holder.itemView.setOnLongClickListener {
            search(item.noteContent)
            true
        }

        holder.itemView.btn_delete.setOnClickListener {
            deleteListener(item)
        }

    }
}

