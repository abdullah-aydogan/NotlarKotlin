package tr.abdullah.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import tr.abdullah.notes.databinding.ActivityUpdateNoteBinding
import java.text.DateFormat
import java.util.Calendar

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var db: NotesDatabaseHelper
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)
        noteId = intent.getIntExtra("note_id", -1)

        if(noteId == -1) {

            finish()
            return
        }

        val note = db.getNoteByID(noteId)

        binding.updateTitleEditText.setText(note.title)
        binding.updateContentEditText.setText(note.content)

        binding.backButton.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.updateSaveButton.setOnClickListener {

            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.updateContentEditText.text.toString()

            val calendar = Calendar.getInstance().time
            val dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(calendar)

            val newdateTime = dateFormat.toString()

            if(newTitle.isEmpty() || newContent.isEmpty()) {

                Toast.makeText(this, R.string.error_text, Toast.LENGTH_SHORT).show()
            }

            else {

                val updateNote = Note(noteId, newTitle, newContent, newdateTime)

                db.updateNote(updateNote)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                Toast.makeText(this, R.string.changes_saved, Toast.LENGTH_SHORT).show()
            }
        }
    }
}