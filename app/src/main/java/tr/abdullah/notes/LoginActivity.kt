package tr.abdullah.notes

import android.app.KeyguardManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import java.util.concurrent.Executors

class LoginActivity : AppCompatActivity() {

    private var fingerprint: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val sp = getSharedPreferences("fingerprint", MODE_PRIVATE)
        fingerprint = sp.getBoolean("finger", false)

        if(!fingerprint) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        else {
            setFingerprint()
        }

        setContentView(R.layout.activity_login)
    }

    private fun isFingerprintAvailable(): Boolean {

        val biometricManager = BiometricManager.from(this)

        return when(biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {

            BiometricManager.BIOMETRIC_SUCCESS -> true

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {

                Toast.makeText(this, R.string.error_no_hardware, Toast.LENGTH_SHORT).show()
                false
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {

                Toast.makeText(this, R.string.error_hw_unawailable, Toast.LENGTH_SHORT).show()
                false
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {

                Toast.makeText(this, R.string.error_none_enrolled, Toast.LENGTH_SHORT).show()
                false
            }

            else -> false
        }
    }

    private fun isFingerprintEnabled(): Boolean {

        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager

        return keyguardManager.isKeyguardSecure && isFingerprintAvailable()
    }

    private fun authenticateWithFingerprint() {

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.fingerprint_title))
            .setSubtitle(getString(R.string.fingerprint_subtitle))
            .setNegativeButtonText(getString(R.string.fingerprint_negative))
            .build()

        val executor = Executors.newSingleThreadExecutor()

        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {

                    super.onAuthenticationError(errorCode, errString)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {

                    super.onAuthenticationSucceeded(result)

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                override fun onAuthenticationFailed() {

                    super.onAuthenticationFailed()
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }

    private fun setFingerprint() {

        if(isFingerprintEnabled()) {

            authenticateWithFingerprint()
        }
    }
}