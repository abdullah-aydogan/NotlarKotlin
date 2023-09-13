package tr.abdullah.notes

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NotesAdapter(private var notes: List<Note>, context: Context)
    : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val db: NotesDatabaseHelper = NotesDatabaseHelper(context)

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val dateTimeTextView: TextView = itemView.findViewById(R.id.dateTimeTextView)
        val noteCardView: CardView = itemView.findViewById(R.id.noteCardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item,
            parent, false)

        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val note = notes[position]

        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content
        holder.dateTimeTextView.text = note.dateTime

        holder.noteCardView.setOnClickListener {

            val intent = Intent(holder.itemView.context, ViewNoteActivity::class.java).apply {

                putExtra("note_id", note.id)
            }

            holder.itemView.context.startActivity(intent)
        }

        holder.noteCardView.setOnLongClickListener {

            val alert = MaterialAlertDialogBuilder(holder.itemView.context)

            alert.setMessage(R.string.alert_text)
            alert.setTitle(R.string.alert_title)
            alert.setIcon(R.drawable.baseline_delete_24)

            alert.setPositiveButton(R.string.delete) { dialogInferface, i ->

                db.deleteNote(note.id)
                refreshData(db.getAllNotes())

                Toast.makeText(holder.itemView.context, R.string.note_deleted, Toast.LENGTH_SHORT).show()
            }

            alert.setNegativeButton(R.string.cancel) {dialogInterface, i ->

                dialogInterface.dismiss()
            }

            alert.create().show()

            return@setOnLongClickListener true
        }
    }

    fun refreshData(newNotes: List<Note>) {

        notes = newNotes

        notifyDataSetChanged()
    }
}