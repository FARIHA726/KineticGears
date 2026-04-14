package com.example.kineticgear2.admin

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
import com.example.kineticgear2.utils.ThemeManager // IMPORT YOUR THEME MANAGER
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var themeManager: ThemeManager // 1. Declare ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        // 2. Initialize and apply the theme BEFORE setting the content view
        themeManager = ThemeManager(this)
        themeManager.applySavedTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        // 3. Drawer & Sidebar Logic
        drawerLayout = findViewById(R.id.drawerLayout)
        val btnMenu = findViewById<ImageView>(R.id.btnMenu)
        val btnSurveillance = findViewById<Button>(R.id.btnSurveillance)

        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // 4. Header & Floating Actions
        val tvLogout = findViewById<TextView>(R.id.tvLogout)
        val fabChat = findViewById<FloatingActionButton>(R.id.fabChat)

        tvLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        btnSurveillance.setOnClickListener {
            showSimpleDialog("Surveillance System", "Accessing industrial camera feed...")
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        fabChat.setOnClickListener {
            showSimpleDialog("Support Chatbot", "Hello! I am your industrial assistant.")
        }

        // 5. Setup the Modules and Theme Toggle
        setupUserManagement()
        setupWorkOrders()
        setupReports()
        setupThemeToggle() // Trigger the dark mode logic
    }

    private fun setupThemeToggle() {
        // NOTE: You must add a TextView or Button with the ID 'btnToggleTheme' to your sidebar XML!
        val btnToggleTheme = findViewById<TextView>(R.id.btnToggleTheme)

        // Using a null check just in case you haven't added it to the XML yet to prevent crashes
        if (btnToggleTheme != null) {
            // Set initial text
            btnToggleTheme.text = if (themeManager.isDarkMode()) "Switch to Light Mode" else "Switch to Dark Mode"

            btnToggleTheme.setOnClickListener {
                val isCurrentlyDark = themeManager.isDarkMode()
                // Flip the theme
                themeManager.setDarkMode(!isCurrentlyDark)

                // Close the drawer before recreating the activity to make it look smoother
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
    }

    private fun setupUserManagement() {
        val module = findViewById<View>(R.id.cardUsers)
        module.findViewById<TextView>(R.id.moduleTitle).text = "User Management"
        module.findViewById<TextView>(R.id.moduleDesc).text = "Manage system access and roles."

        val btnAdd = module.findViewById<Button>(R.id.btn1)
        btnAdd.text = "Add User"
        btnAdd.setOnClickListener {
            startActivity(Intent(this, UserManagementActivity::class.java))
        }

        module.findViewById<Button>(R.id.btn2).text = "Edit User"
        module.findViewById<Button>(R.id.btn3).text = "Delete User"
    }

    private fun setupWorkOrders() {
        val module = findViewById<View>(R.id.cardOrders)
        module.findViewById<TextView>(R.id.moduleTitle).text = "Work Orders"
        module.findViewById<TextView>(R.id.moduleDesc).text = "Track manufacturing production."

        val btnCreate = module.findViewById<Button>(R.id.btn1)
        btnCreate.text = "Create Order"
        // Ensure you change these hardcoded colors to your resource colors for Dark Mode to work optimally
        btnCreate.setBackgroundColor(getColor(R.color.industrial_red))
        btnCreate.setTextColor(getColor(R.color.text_primary))

        module.findViewById<Button>(R.id.btn2).text = "Edit Order"
        module.findViewById<Button>(R.id.btn3).text = "Delete Order"
    }

    private fun setupReports() {
        val module = findViewById<View>(R.id.cardReportsModule)
        module.findViewById<TextView>(R.id.moduleTitle).text = "Reports"
        module.findViewById<TextView>(R.id.moduleDesc).text = "Production fulfillment analytics."

        module.findViewById<Button>(R.id.btn1).text = "Production Reports"
        module.findViewById<Button>(R.id.btn2).text = "Work Order Reports"
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