package com.example.kineticgear2.operator

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.kineticgear2.R
import com.example.kineticgear2.authentication.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class OperatorDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operator_dashboard)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val btnMenu = findViewById<ImageView>(R.id.btnMenu)
        val tvLogout = findViewById<TextView>(R.id.tvLogout)

        // Sidebar Sliding Logic
        btnMenu.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }

        // Logout Implementation
        tvLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        setupOperatorModules()

        findViewById<View>(R.id.fabChat).setOnClickListener {
            showSimpleDialog("Assistant", "How can I help with your current production job?")
        }
    }

    private fun setupOperatorModules() {
        // Card 1: QR Scanning
        val qrCard = findViewById<View>(R.id.cardQRScanner)
        qrCard.findViewById<TextView>(R.id.moduleTitle).text = "QR Code Scan"
        qrCard.findViewById<TextView>(R.id.moduleDesc).text = "Scan Machine/Job Card to begin."
        qrCard.findViewById<Button>(R.id.btn1).apply {
            text = "SCAN QR"
            setBackgroundColor(getColor(android.R.color.holo_red_dark))
            setTextColor(getColor(android.R.color.white))
            setOnClickListener { showSimpleDialog("Camera", "Initializing QR Scanner...") }
        }

        // Card 2: Start/Stop Job
        val controlCard = findViewById<View>(R.id.cardJobControl)
        controlCard.findViewById<TextView>(R.id.moduleTitle).text = "Start/Stop Job"
        controlCard.findViewById<TextView>(R.id.moduleDesc).text = "Update the live status of your job."
        controlCard.findViewById<Button>(R.id.btn1).text = "START"
        controlCard.findViewById<Button>(R.id.btn2).text = "STOP"
        controlCard.findViewById<Button>(R.id.btn3).text = "PAUSE"

        // Card 3: View Assigned Job
        val detailsCard = findViewById<View>(R.id.cardJobDetails)
        detailsCard.findViewById<TextView>(R.id.moduleTitle).text = "View Assigned Job"
        detailsCard.findViewById<TextView>(R.id.moduleDesc).text = "Check specific details of current tasks."
        detailsCard.findViewById<Button>(R.id.btn1).text = "CHECK DETAILS"
    }

    private fun showSimpleDialog(title: String, message: String) {
        AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton("OK", null).show()
    }
}