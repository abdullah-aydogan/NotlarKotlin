package tr.abdullah.notes

import android.app.KeyguardManager
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import tr.abdullah.notes.databinding.ActivityMainBinding
import java.util.Locale
import java.util.concurrent.Executors

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

            val sp = getSharedPreferences("fingerprint", MODE_PRIVATE)
            val editor = sp.edit()

            val alert = MaterialAlertDialogBuilder(this)

            alert.setMessage(R.string.finger_alert_message)
            alert.setTitle(R.string.finger_alert_title)
            alert.setIcon(R.drawable.baseline_fingerprint_24)

            alert.setPositiveButton(R.string.finger_alert_positive) { dialogInferface, i ->

                editor.putBoolean("finger", true)
                editor.commit()
            }

            alert.setNegativeButton(R.string.finger_alert_negative) {dialogInterface, i ->

                editor.putBoolean("finger", false)
                editor.commit()
            }

            alert.create().show()
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


    override fun onResume() {

        super.onResume()
        notesAdapter.refreshData(db.getAllNotes())
    }
}