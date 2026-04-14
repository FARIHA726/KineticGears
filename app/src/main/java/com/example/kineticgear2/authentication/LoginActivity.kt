package com.example.kineticgear2.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kineticgear2.R
import com.example.kineticgear2.utils.ThemeManager // IMPORT THE THEME MANAGER

// IMPORT YOUR DASHBOARDS FROM THEIR PACKAGES
import com.example.kineticgear2.admin.AdminDashboardActivity
import com.example.kineticgear2.operator.OperatorDashboardActivity
import com.example.kineticgear2.supervisor.SupervisorDashboardActivity
import com.example.kineticgear2.quality.QADashboardActivity
import com.example.kineticgear2.stores.StoresDashboardActivity

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. APPLY THEME BEFORE ANYTHING ELSE TO PREVENT FLASHING
        ThemeManager(this).applySavedTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val etEmail = findViewById<EditText>(R.id.loginEmail)
        val etPassword = findViewById<EditText>(R.id.loginPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvSignupLink = findViewById<TextView>(R.id.tvGoToSignup)

        tvSignupLink.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnLogin.isEnabled = false

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val userId = result.user?.uid
                    if (userId != null) {
                        checkUserRoleAndNavigate(userId)
                    }
                }
                .addOnFailureListener {
                    btnLogin.isEnabled = true
                    Toast.makeText(this, "Login Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun checkUserRoleAndNavigate(uid: String) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Get the role and handle null/empty cases safely
                    val role = document.getString("role") ?: "Unknown"

                    // 2. USING ignoreCase = true PREVENTS TYPOS FROM BREAKING THE APP
                    val intent = when {
                        role.equals("Admin", ignoreCase = true) -> Intent(this, AdminDashboardActivity::class.java)
                        role.equals("Operator", ignoreCase = true) -> Intent(this, OperatorDashboardActivity::class.java)
                        role.equals("Supervisor", ignoreCase = true) -> Intent(this, SupervisorDashboardActivity::class.java)
                        role.equals("QA/QC", ignoreCase = true) -> Intent(this, QADashboardActivity::class.java)
                        role.equals("Stores", ignoreCase = true) -> Intent(this, StoresDashboardActivity::class.java)
                        else -> {
                            // Helpful for debugging: tells you exactly what string is in the DB
                            Toast.makeText(this, "Unrecognized role: '$role'. Contact Admin.", Toast.LENGTH_LONG).show()
                            null
                        }
                    }

                    if (intent != null) {
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        findViewById<Button>(R.id.btnLogin).isEnabled = true
                    }
                } else {
                    findViewById<Button>(R.id.btnLogin).isEnabled = true
                    Toast.makeText(this, "No profile found in database for this user.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                findViewById<Button>(R.id.btnLogin).isEnabled = true
                Toast.makeText(this, "Database Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}