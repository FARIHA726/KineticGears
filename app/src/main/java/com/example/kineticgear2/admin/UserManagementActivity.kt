package com.example.kineticgear2.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kineticgear2.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class UserManagementActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_management)

        db = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.rvUserList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val btnAddUser = findViewById<FloatingActionButton>(R.id.btnAddUser)

        btnAddUser.setOnClickListener {
            // Logic to open a dialog or new activity to add a user
            Toast.makeText(this, "Opening Add User Form...", Toast.LENGTH_SHORT).show()
        }

        fetchUsers()
    }

    private fun fetchUsers() {
        db.collection("users").get()
            .addOnSuccessListener { documents ->
                // Here you would pass 'documents' to a RecyclerView Adapter
                Toast.makeText(this, "Found ${documents.size()} users", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error fetching users", Toast.LENGTH_SHORT).show()
            }
    }
}
