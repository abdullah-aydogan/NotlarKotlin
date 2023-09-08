package tr.abdullah.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tr.abdullah.notes.databinding.ActivityViewNoteBinding

class ViewNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewNoteBinding
    private lateinit var db: NotesDatabaseHelper
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityViewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)
        noteId = intent.getIntExtra("note_id", -1)

        if(noteId == -1) {

            finish()
            return
        }

        val note = db.getNoteByID(noteId)

        binding.noteHeading.text = note.title
        binding.noteContent.setText(note.content)
    }
}