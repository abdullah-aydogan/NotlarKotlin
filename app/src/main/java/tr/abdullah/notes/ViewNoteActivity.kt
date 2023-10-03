package tr.abdullah.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import tr.abdullah.notes.databinding.ActivityViewNoteBinding

class ViewNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewNoteBinding
    private lateinit var db: NotesDatabaseHelper
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityViewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gelenVeri = intent.getIntExtra("note_id", noteId)

        binding.backButton.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.updateButton.setOnClickListener {

            val intent = Intent(this, UpdateNoteActivity::class.java).apply {

                putExtra("note_id", gelenVeri)
            }

            startActivity(intent)
        }

        binding.deleteButton.setOnClickListener {

            val alert = AlertDialog.Builder(this)
            val intent = Intent(this, MainActivity::class.java)

            alert.setMessage(R.string.alert_text)
            alert.setTitle(R.string.alert_title)
            alert.setIcon(R.drawable.baseline_delete_24)

            alert.setPositiveButton(R.string.delete) { dialogInferface, i ->

                db.deleteNote(gelenVeri)

                startActivity(intent)

                Toast.makeText(this, R.string.note_deleted, Toast.LENGTH_SHORT).show()
            }

            alert.setNegativeButton(R.string.cancel) {dialogInterface, i ->

                dialogInterface.dismiss()
            }

            alert.create().show()
        }

        db = NotesDatabaseHelper(this)
        noteId = intent.getIntExtra("note_id", -1)

        if(noteId == -1) {

            finish()
            return
        }

        val note = db.getNoteByID(noteId)

        binding.noteHeading.text = note.title
        binding.noteContent.text = note.content

        binding.noteContent.movementMethod = ScrollingMovementMethod()
    }
}