package com.example.kineticgear2.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kineticgear2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Setup Role Dropdown
        val roles = arrayOf("Admin","Operator", "Supervisor", "QA/QC", "Stores")
        val spinner: Spinner = findViewById(R.id.spinnerRole)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        spinner.adapter = adapter

        // View Bindings
        val etName = findViewById<EditText>(R.id.signupName)
        val etEmail = findViewById<EditText>(R.id.signupEmail)
        val etPassword = findViewById<EditText>(R.id.signupPassword)
        val btnRegister = findViewById<Button>(R.id.btnSignup)
        val tvLoginLink = findViewById<TextView>(R.id.tvGoToLogin)

        // Link to Login Page
        tvLoginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val role = spinner.selectedItem.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 1. Create User in Auth
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    // 2. Get the Unique ID (UID) created by Firebase
                    val userId = result.user?.uid

                    // 3. Create a User Map to save to Firestore
                    val userMap = hashMapOf(
                        "uid" to userId,
                        "fullName" to name,
                        "email" to email,
                        "role" to role // This saves "Operator" or "Supervisor" etc.
                    )

                    // 4. Save to Firestore Database
                    if (userId != null) {
                        db.collection("users").document(userId).set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Account Created Successfully!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Registration Failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}