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
import com.example.kineticgear2.utils.ThemeManager
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize and apply the theme BEFORE setting the content view
        themeManager = ThemeManager(this)
        themeManager.applySavedTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        // Drawer & Sidebar Logic
        drawerLayout = findViewById(R.id.drawerLayout)
        val btnMenu = findViewById<ImageView>(R.id.btnMenu)

        // Find views inside the navigation view safely
        val navView = findViewById<NavigationView>(R.id.navView)
        val btnSurveillance = navView.findViewById<Button>(R.id.btnSurveillance)

        // --- FIXED LOGOUT LOGIC ---
        // Find the Logout button INSIDE the navigation view, not the main layout
        val tvLogout = navView.findViewById<TextView>(R.id.tvLogout)

        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Floating Action Button
        val fabChat = findViewById<FloatingActionButton>(R.id.fabChat)

        // Apply safe call (?) to prevent NullPointerException
        tvLogout?.setOnClickListener {
            // Close the drawer smoothly before logging out
            drawerLayout.closeDrawer(GravityCompat.START)

            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        btnSurveillance?.setOnClickListener {
            showSimpleDialog("Surveillance System", "Accessing industrial camera feed...")
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        fabChat.setOnClickListener {
            showSimpleDialog("Support Chatbot", "Hello! I am your industrial assistant.")
        }

        // Setup the Modules and Theme Toggle
        setupUserManagement()
        setupWorkOrders()
        setupReports()
        setupThemeToggle()
    }

    // --- Expandable Card Click Logic ---
    private fun setupExpandableCard(view: View) {
        val headerArea = view.findViewById<View>(R.id.cardHeaderArea)
        val expandableContainer = view.findViewById<View>(R.id.expandableActionContainer)
        val ivChevron = view.findViewById<ImageView>(R.id.ivChevron)

        headerArea.setOnClickListener {
            if (expandableContainer.visibility == View.GONE) {
                expandableContainer.visibility = View.VISIBLE
                // Rotate chevron when open
                ivChevron.rotation = 90f
            } else {
                expandableContainer.visibility = View.GONE
                // Reset chevron rotation
                ivChevron.rotation = 0f
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

    private fun setupUserManagement() {
        val module = findViewById<View>(R.id.cardUsers)

        // Text & Descriptions
        module.findViewById<TextView>(R.id.moduleTitle).text = "User Management"
        module.findViewById<TextView>(R.id.moduleDesc).text = "Add / Edit / Delete\nUsers & Assign Roles"

        // Setup Badges
        module.findViewById<TextView>(R.id.badge1).text = "24 ACTIVE"
        module.findViewById<TextView>(R.id.badge2).text = "3 PENDING"

        // Enable Expandable Logic
        setupExpandableCard(module)

        // Buttons
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

        module.findViewById<TextView>(R.id.moduleTitle).text = "Work Order Management"
        module.findViewById<TextView>(R.id.moduleDesc).text = "Create / Edit /\nDelete Work Orders"

        // Setup Badges
        module.findViewById<TextView>(R.id.badge1).text = "6 ACTIVE"
        val badge2 = module.findViewById<TextView>(R.id.badge2)
        badge2.text = "2 DELAYED"
        badge2.setTextColor(getColor(android.R.color.holo_orange_dark)) // Highlight warning

        // Enable Expandable Logic
        setupExpandableCard(module)

        // Buttons
        module.findViewById<Button>(R.id.btn1).text = "Create Order"
        module.findViewById<Button>(R.id.btn2).text = "Edit Order"
        module.findViewById<Button>(R.id.btn3).text = "Delete Order"
    }

    private fun setupReports() {
        val module = findViewById<View>(R.id.cardReportsModule)

        module.findViewById<TextView>(R.id.moduleTitle).text = "Reports"
        module.findViewById<TextView>(R.id.moduleDesc).text = "View Production &\nWork Order Reports"

        // Setup Badges
        module.findViewById<TextView>(R.id.badge1).text = "74% TARGET"
        val badge2 = module.findViewById<TextView>(R.id.badge2)
        badge2.text = "ON TRACK"
        badge2.setTextColor(getColor(android.R.color.holo_green_dark))

        // Enable Expandable Logic
        setupExpandableCard(module)

        // Buttons
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