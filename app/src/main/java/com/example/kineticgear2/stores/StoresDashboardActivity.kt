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
import com.google.firebase.auth.FirebaseAuth

class StoresDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stores_dashboard)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val btnMenu = findViewById<ImageView>(R.id.btnMenu)
        val tvLogout = findViewById<TextView>(R.id.tvLogout)
        val btnCamera = findViewById<ImageView>(R.id.btnHeaderCamera)

        // Sidebar Navigation
        btnMenu.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }

        // Logout
        tvLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
        }

        // Camera / QR Scan
        btnCamera.setOnClickListener {
            showSimpleDialog("Inventory Scanner", "Scanning QR for Work Order / Material ID...")
        }

        setupIssueCard()
        setupReceiveCard()
        setupTraceabilityCard()

        findViewById<View>(R.id.fabChat).setOnClickListener {
            showSimpleDialog("Inventory Bot", "Try asking: 'Check stock of Item A' or 'Show pending issues'")
        }
    }

    private fun setupIssueCard() {
        val view = findViewById<View>(R.id.cardIssueMaterial)
        view.findViewById<TextView>(R.id.moduleTitle).text = "Issue Raw Material"
        view.findViewById<TextView>(R.id.moduleDesc).text = "📦 Issue material for jobs and auto-update stock."

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
        view.findViewById<TextView>(R.id.moduleDesc).text = "📥 Record goods received from production."

        view.findViewById<Button>(R.id.btn1).text = "RECEIVE ENTRY"
        view.findViewById<Button>(R.id.btn2).text = "GENERATE GRN"
        view.findViewById<Button>(R.id.btn3).text = "QUALITY STATUS"
    }

    private fun setupTraceabilityCard() {
        val view = findViewById<View>(R.id.cardTraceability)
        view.findViewById<TextView>(R.id.moduleTitle).text = "Material Traceability"
        view.findViewById<TextView>(R.id.moduleDesc).text = "🔍 Track movement by Order, Batch, or Code."

        view.findViewById<Button>(R.id.btn1).text = "SEARCH HISTORY"
        view.findViewById<Button>(R.id.btn2).text = "EXPORT REPORT"
        view.findViewById<Button>(R.id.btn3).text = "PRINT LABELS"
    }

    private fun showSimpleDialog(title: String, message: String) {
        AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton("OK", null).show()
    }
}