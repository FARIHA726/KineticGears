package com.example.kineticgear2.quality

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

class QADashboardActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize and apply theme BEFORE setting layout
        themeManager = ThemeManager(this)
        themeManager.applySavedTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qa_dashboard)

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
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
        }

        findViewById<FloatingActionButton>(R.id.fabChat).setOnClickListener {
            showSimpleDialog("QA Assistant", "Hello! How can I help with quality audits today?")
        }

        // Setup the Modules with Expandable Logic
        setupInspectionCard()
        setupApprovalCard()
        setupNCRCard()
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

    private fun setupInspectionCard() {
        val view = findViewById<View>(R.id.cardRecordInspection)

        view.findViewById<TextView>(R.id.moduleTitle).text = "Record Inspection"
        view.findViewById<TextView>(R.id.moduleDesc).text = "Enter Job ID, Inspector, and Remarks."

        // Badges
        view.findViewById<TextView>(R.id.badge1).text = "5 PENDING"
        view.findViewById<TextView>(R.id.badge2).visibility = View.GONE

        setupExpandableCard(view)

        view.findViewById<Button>(R.id.btn1).apply {
            text = "NEW INSPECTION"
            // The XML already sets the button to industrial_red, but we preserve your click listener:
            setOnClickListener { showSimpleDialog("Camera", "Scanning Work Order QR...") }
        }
        view.findViewById<Button>(R.id.btn2).text = "ADD REMARKS"
        view.findViewById<Button>(R.id.btn3).text = "VIEW HISTORY"
    }

    private fun setupApprovalCard() {
        val view = findViewById<View>(R.id.cardApproval)

        view.findViewById<TextView>(R.id.moduleTitle).text = "Approval / Result"
        view.findViewById<TextView>(R.id.moduleDesc).text = "Review and finalize job status."

        // Badges
        val badge1 = view.findViewById<TextView>(R.id.badge1)
        badge1.text = "12 READY"
        badge1.setTextColor(getColor(android.R.color.holo_green_dark))

        val badge2 = view.findViewById<TextView>(R.id.badge2)
        badge2.text = "2 REJECTED"
        badge2.setTextColor(getColor(android.R.color.holo_red_dark))

        setupExpandableCard(view)

        view.findViewById<Button>(R.id.btn1).text = "APPROVE JOB"
        view.findViewById<Button>(R.id.btn2).text = "REJECT JOB"
        view.findViewById<Button>(R.id.btn3).text = "AUTO-UPDATE STATUS"
    }

    private fun setupNCRCard() {
        val view = findViewById<View>(R.id.cardNCR)

        view.findViewById<TextView>(R.id.moduleTitle).text = "NCR Linkage"
        view.findViewById<TextView>(R.id.moduleDesc).text = "Manage Non-Conformance Reports."

        // Badges
        val badge1 = view.findViewById<TextView>(R.id.badge1)
        badge1.text = "3 ACTIVE NCR"
        badge1.setTextColor(getColor(android.R.color.holo_orange_dark))

        view.findViewById<TextView>(R.id.badge2).visibility = View.GONE

        setupExpandableCard(view)

        view.findViewById<Button>(R.id.btn1).text = "CREATE NCR"
        view.findViewById<Button>(R.id.btn2).text = "VIEW LINKED NCR"
        view.findViewById<Button>(R.id.btn3).text = "MARK AS RESOLVED"
    }

    private fun showSimpleDialog(title: String, message: String) {
        AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton("OK", null).show()
    }
}