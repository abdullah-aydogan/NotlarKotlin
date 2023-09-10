package tr.abdullah.notes

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import tr.abdullah.notes.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: NotesDatabaseHelper
    private lateinit var notesAdapter: NotesAdapter

    private val languages = arrayOf("Türkçe", "English")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)
        notesAdapter = NotesAdapter(db.getAllNotes(), this)

        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerView.adapter = notesAdapter

        binding.addButton.setOnClickListener {

            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        binding.languageButton.setOnClickListener {

            changeLanguage()
        }

        binding.fingerPrintButton.setOnClickListener {

            setFingerprint()
        }
    }

    private fun changeLanguage() {

        val checkedItem: Int = getString(R.string.checkedItem).toInt()
        val alert = MaterialAlertDialogBuilder(this)

        alert.setTitle(R.string.languages)

        alert.setSingleChoiceItems(languages, checkedItem) { dialogInterface, i ->

            when (i) {

                0 -> {
                    setLanguage("TR")
                    recreate()
                }

                1 -> {
                    setLanguage("EN")
                    recreate()
                }
            }

            dialogInterface.dismiss()
        }

        alert.setNegativeButton(R.string.cancel) { dialogInterface, i ->

            dialogInterface.dismiss()
        }

        alert.create().show()
    }

    private fun setLanguage(language: String) {

        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration()
        config.locale = locale

        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }

    private fun setFingerprint() {


    }

    override fun onResume() {

        super.onResume()
        notesAdapter.refreshData(db.getAllNotes())
    }
}