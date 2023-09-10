package tr.abdullah.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import tr.abdullah.notes.databinding.ActivityAddNoteBinding
import java.text.DateFormat
import java.util.Calendar

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: NotesDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)

        binding.saveButton.setOnClickListener {

            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()

            val calendar = Calendar.getInstance().time
            val dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(calendar)

            val dateTime = dateFormat.toString()

            if(title.isEmpty() || content.isEmpty()) {

                Toast.makeText(this, R.string.error_text, Toast.LENGTH_SHORT).show()
            }

            else {

                val note = Note(0, title, content, dateTime)

                db.insertNote(note)
                finish()

                Toast.makeText(this, R.string.note_saved, Toast.LENGTH_SHORT).show()
            }
        }
    }
}