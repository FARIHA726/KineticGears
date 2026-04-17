package com.example.kineticgear2.stores

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

class StoresDashboardActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize and apply theme BEFORE setting layout
        themeManager = ThemeManager(this)
        themeManager.applySavedTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stores_dashboard)

        drawerLayout = findViewById(R.id.drawerLayout)
        val btnMenu = findViewById<ImageView>(R.id.btnMenu)
        val btnCamera = findViewById<ImageView>(R.id.btnHeaderCamera)

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

        // Camera / QR Scan (From Header)
        btnCamera.setOnClickListener {
            showSimpleDialog("Inventory Scanner", "Scanning QR for Work Order / Material ID...")
        }

        findViewById<FloatingActionButton>(R.id.fabChat).setOnClickListener {
            showSimpleDialog("Inventory Bot", "Try asking: 'Check stock of Item A' or 'Show pending issues'")
        }

        // Setup the Modules with Expandable Logic
        setupIssueCard()
        setupReceiveCard()
        setupTraceabilityCard()
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

    private fun setupIssueCard() {
        val view = findViewById<View>(R.id.cardIssueMaterial)

        view.findViewById<TextView>(R.id.moduleTitle).text = "Issue Raw Material"
        view.findViewById<TextView>(R.id.moduleDesc).text = "📦 Issue material for jobs and\nauto-update stock."

        // Badges
        view.findViewById<TextView>(R.id.badge1).text = "8 PENDING"
        view.findViewById<TextView>(R.id.badge2).visibility = View.GONE

        setupExpandableCard(view)

        view.findViewById<Button>(R.id.btn1).apply {
            text = "ISSUE MATERIAL"
            setTextColor(getColor(android.R.color.white))
            setBackgroundColor(getColor(android.R.color.holo_red_dark))
        }
        view.findViewById<Button>(R.id.btn2).text = "GENERATE SLIP"
        view.findViewById<Button>(R.id.btn3).text = "STOCK ADJUSTMENT"
    }

    private fun setupReceiveCard() {
        val view = findViewById<View>(R.id.cardReceiveFG)

        view.findViewById<TextView>(R.id.moduleTitle).text = "Receive Finished Goods"
        view.findViewById<TextView>(R.id.moduleDesc).text = "📥 Record goods received from\nthe production floor."

        // Badges
        val badge1 = view.findViewById<TextView>(R.id.badge1)
        badge1.text = "5 INBOUND"
        badge1.setTextColor(getColor(android.R.color.holo_blue_dark))

        view.findViewById<TextView>(R.id.badge2).visibility = View.GONE

        setupExpandableCard(view)

        view.findViewById<Button>(R.id.btn1).text = "RECEIVE ENTRY"
        view.findViewById<Button>(R.id.btn2).text = "GENERATE GRN"
        view.findViewById<Button>(R.id.btn3).text = "QUALITY STATUS"
    }

    private fun setupTraceabilityCard() {
        val view = findViewById<View>(R.id.cardTraceability)

        view.findViewById<TextView>(R.id.moduleTitle).text = "Material Traceability"
        view.findViewById<TextView>(R.id.moduleDesc).text = "🔍 Track movement by Order,\nBatch, or Material Code."

        // Badges
        val badge1 = view.findViewById<TextView>(R.id.badge1)
        badge1.text = "SYSTEM ONLINE"
        badge1.setTextColor(getColor(android.R.color.holo_green_dark))

        view.findViewById<TextView>(R.id.badge2).visibility = View.GONE

        setupExpandableCard(view)

        view.findViewById<Button>(R.id.btn1).text = "SEARCH HISTORY"
        view.findViewById<Button>(R.id.btn2).text = "EXPORT REPORT"
        view.findViewById<Button>(R.id.btn3).text = "PRINT LABELS"
    }

    private fun showSimpleDialog(title: String, message: String) {
        AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton("OK", null).show()
    }
}