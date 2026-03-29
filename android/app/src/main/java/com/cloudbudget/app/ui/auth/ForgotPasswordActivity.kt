package com.cloudbudget.app.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cloudbudget.app.R
import com.cloudbudget.app.data.DemoPreferences
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var btnSendReset: TextView
    private lateinit var btnBackToSignIn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        findViewById<TextView>(R.id.tvScreenTitle).setText(R.string.title_forgot_password)
        findViewById<TextView>(R.id.btnBack).setOnClickListener { finish() }

        etEmail = findViewById(R.id.etResetEmail)
        btnSendReset = findViewById(R.id.btnSendReset)
        btnBackToSignIn = findViewById(R.id.btnBackToSignIn)

        val prefill = intent.getStringExtra(EXTRA_PREFILL_EMAIL).orEmpty()
        if (prefill.isNotBlank()) {
            etEmail.setText(prefill)
            etEmail.setSelection(prefill.length)
        }

        btnSendReset.setOnClickListener { sendResetLink() }
        btnBackToSignIn.setOnClickListener { finish() }
    }

    private fun sendResetLink() {
        val email = etEmail.text?.toString().orEmpty().trim()
        if (email.isBlank()) {
            Toast.makeText(this, R.string.error_reset_email_required, Toast.LENGTH_SHORT).show()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, R.string.error_invalid_email, Toast.LENGTH_SHORT).show()
            return
        }

        setLoading(true)
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                setLoading(false)
                if (task.isSuccessful) {
                    Toast.makeText(this, R.string.reset_link_sent, Toast.LENGTH_LONG).show()
                    finish()
                    return@addOnCompleteListener
                }

                // Demo fallback keeps flow usable when auth backend isn't configured.
                val demoEmail = DemoPreferences.getRegisteredEmail(this)
                if (demoEmail.equals(email.lowercase(), ignoreCase = true)) {
                    Toast.makeText(this, R.string.reset_link_sent_demo, Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, R.string.error_reset_failed, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun setLoading(loading: Boolean) {
        btnSendReset.isEnabled = !loading
        btnSendReset.text = getString(if (loading) R.string.sending_reset_link else R.string.send_reset_link)
    }

    companion object {
        const val EXTRA_PREFILL_EMAIL = "extra_prefill_email"
    }
}
