package com.example.kineticgear2.supervisor

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

class SupervisorDashboardActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize and apply theme BEFORE setting layout to prevent white flashes
        themeManager = ThemeManager(this)
        themeManager.applySavedTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supervisor_dashboard)

        drawerLayout = findViewById(R.id.drawerLayout)
        val btnMenu = findViewById<ImageView>(R.id.btnMenu)

        // Setup Sidebar Views Safely from the NavigationView
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

        findViewById<FloatingActionButton>(R.id.fabChat).setOnClickListener {
            showDialog("Chatbot", "Supervisor Assistant: How can I help with floor operations?")
        }

        // Setup the Modules with Expandable Logic
        setupWorkOrderCard()
        setupMachineCard()
        setupAlertCard()
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

    // --- DARK MODE TOGGLE ---
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

    private fun setupWorkOrderCard() {
        val view = findViewById<View>(R.id.cardAssignedJobs)

        view.findViewById<TextView>(R.id.moduleTitle).text = "Work Orders"
        view.findViewById<TextView>(R.id.moduleDesc).text = "View assigned jobs and production status."

        // Badges
        view.findViewById<TextView>(R.id.badge1).text = "18 ACTIVE"
        val badge2 = view.findViewById<TextView>(R.id.badge2)
        badge2.text = "2 PENDING"
        badge2.setTextColor(getColor(android.R.color.holo_orange_dark))

        setupExpandableCard(view)

        view.findViewById<Button>(R.id.btn1).text = "View All Jobs"
        view.findViewById<Button>(R.id.btn2).text = "Pending Approvals"
        view.findViewById<Button>(R.id.btn3).text = "Update Status"
    }

    private fun setupMachineCard() {
        val view = findViewById<View>(R.id.cardMachineStatus)

        view.findViewById<TextView>(R.id.moduleTitle).text = "Machine Tracking"
        view.findViewById<TextView>(R.id.moduleDesc).text = "Live progress and health of floor machines."

        // Badges
        view.findViewById<TextView>(R.id.badge1).text = "LINE NOMINAL"
        view.findViewById<TextView>(R.id.badge2).visibility = View.GONE

        setupExpandableCard(view)

        view.findViewById<Button>(R.id.btn1).text = "Live Telemetry"
        view.findViewById<Button>(R.id.btn2).text = "Maintenance Logs"
        view.findViewById<Button>(R.id.btn3).text = "Machine Health"
    }

    private fun setupAlertCard() {
        val view = findViewById<View>(R.id.cardAlerts)

        view.findViewById<TextView>(R.id.moduleTitle).text = "Issues & Alerts"
        view.findViewById<TextView>(R.id.moduleDesc).text = "Track production delays and problem alerts."

        // Badges
        val badge1 = view.findViewById<TextView>(R.id.badge1)
        badge1.text = "2 PENDING ALERTS"
        badge1.setTextColor(getColor(android.R.color.holo_orange_dark))

        val badge2 = view.findViewById<TextView>(R.id.badge2)
        badge2.text = "0 NCR"
        badge2.setTextColor(getColor(android.R.color.holo_green_dark))

        setupExpandableCard(view)

        view.findViewById<Button>(R.id.btn1).apply {
            text = "REPORT DELAY"
            setBackgroundColor(getColor(android.R.color.holo_red_dark)) // Used red instead of orange for a more modern alert feel
            setTextColor(getColor(android.R.color.white))
        }
        view.findViewById<Button>(R.id.btn2).text = "Safety Alerts"
        view.findViewById<Button>(R.id.btn3).text = "Critical Issues"
    }

    private fun showDialog(title: String, msg: String) {
        AlertDialog.Builder(this).setTitle(title).setMessage(msg).setPositiveButton("OK", null).show()
    }
}