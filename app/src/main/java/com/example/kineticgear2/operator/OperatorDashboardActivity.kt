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
import com.example.kineticgear2.utils.ThemeManager
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class OperatorDashboardActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize and apply the theme BEFORE setting the content view
        themeManager = ThemeManager(this)
        themeManager.applySavedTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operator_dashboard)

        drawerLayout = findViewById(R.id.drawerLayout)
        val btnMenu = findViewById<ImageView>(R.id.btnMenu)

        // Find views inside the navigation view safely
        val navView = findViewById<NavigationView>(R.id.navView)
        val tvLogout = navView.findViewById<TextView>(R.id.tvLogout)

        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // --- SAFE LOGOUT LOGIC ---
        tvLogout?.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        val fabChat = findViewById<FloatingActionButton>(R.id.fabChat)
        fabChat.setOnClickListener {
            showSimpleDialog("Support Chatbot", "How can I help with your current production job?")
        }

        // Setup the Modules and Theme Toggle
        setupQRScanner()
        setupJobControl()
        setupJobDetails()
        setupThemeToggle()
    }

    // --- EXPANDABLE CARD CLICK LOGIC ---
    private fun setupExpandableCard(view: View) {
        val headerArea = view.findViewById<View>(R.id.cardHeaderArea)
        val expandableContainer = view.findViewById<View>(R.id.expandableActionContainer)
        val ivChevron = view.findViewById<ImageView>(R.id.ivChevron)

        headerArea.setOnClickListener {
            if (expandableContainer.visibility == View.GONE) {
                expandableContainer.visibility = View.VISIBLE
                ivChevron.rotation = 90f // Rotate chevron when open
            } else {
                expandableContainer.visibility = View.GONE
                ivChevron.rotation = 0f // Reset chevron rotation
            }
        }
    }

    private fun setupThemeToggle() {
        val navView = findViewById<NavigationView>(R.id.navView)
        val btnToggleTheme = navView?.findViewById<TextView>(R.id.btnToggleTheme)

        if (btnToggleTheme != null) {
            btnToggleTheme.text = if (themeManager.isDarkMode()) "Switch to Light Mode" else "Switch to Dark Mode"

            btnToggleTheme.setOnClickListener {
                val isCurrentlyDark = themeManager.isDarkMode()
                themeManager.setDarkMode(!isCurrentlyDark)
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
    }

    private fun setupQRScanner() {
        val module = findViewById<View>(R.id.cardQRScanner)

        module.findViewById<TextView>(R.id.moduleTitle).text = "QR Code Scan"
        module.findViewById<TextView>(R.id.moduleDesc).text = "Scan Machine or Job Card\nto begin your shift."

        // Setup Dummy Badges
        module.findViewById<TextView>(R.id.badge1).text = "READY"
        val badge2 = module.findViewById<TextView>(R.id.badge2)
        badge2.text = "AWAITING SCAN"
        badge2.setTextColor(getColor(android.R.color.holo_orange_dark))

        // Enable Expandable Logic
        setupExpandableCard(module)

        // Buttons
        val btn1 = module.findViewById<Button>(R.id.btn1)
        btn1.text = "SCAN QR"
        btn1.setOnClickListener { showSimpleDialog("Camera", "Initializing QR Scanner...") }

        // Hide unused buttons
        module.findViewById<Button>(R.id.btn2).visibility = View.GONE
        module.findViewById<Button>(R.id.btn3).visibility = View.GONE
    }

    private fun setupJobControl() {
        val module = findViewById<View>(R.id.cardJobControl)

        module.findViewById<TextView>(R.id.moduleTitle).text = "Job Control"
        module.findViewById<TextView>(R.id.moduleDesc).text = "Update the live status of\nyour current operation."

        module.findViewById<TextView>(R.id.badge1).text = "MACHINE IDLE"
        module.findViewById<TextView>(R.id.badge2).visibility = View.GONE

        // Enable Expandable Logic
        setupExpandableCard(module)

        // Buttons
        module.findViewById<Button>(R.id.btn1).text = "START JOB"
        module.findViewById<Button>(R.id.btn2).text = "PAUSE JOB"

        val btn3 = module.findViewById<Button>(R.id.btn3)
        btn3.text = "STOP JOB"
        btn3.setTextColor(getColor(android.R.color.holo_red_dark))
    }

    private fun setupJobDetails() {
        val module = findViewById<View>(R.id.cardJobDetails)

        module.findViewById<TextView>(R.id.moduleTitle).text = "Assigned Tasks"
        module.findViewById<TextView>(R.id.moduleDesc).text = "Check specific details of\ncurrent work orders."

        module.findViewById<TextView>(R.id.badge1).text = "1 ASSIGNED"
        val badge2 = module.findViewById<TextView>(R.id.badge2)
        badge2.text = "DUE TODAY"
        badge2.setTextColor(getColor(android.R.color.holo_green_dark))

        // Enable Expandable Logic
        setupExpandableCard(module)

        // Buttons
        module.findViewById<Button>(R.id.btn1).text = "CHECK DETAILS"
        module.findViewById<Button>(R.id.btn2).visibility = View.GONE
        module.findViewById<Button>(R.id.btn3).visibility = View.GONE
    }

    private fun showSimpleDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}