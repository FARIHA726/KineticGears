package com.example.kineticgear2.supervisor

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

class SupervisorDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supervisor_dashboard)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val btnMenu = findViewById<ImageView>(R.id.btnMenu)

        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        setupWorkOrderCard()
        setupMachineCard()
        setupAlertCard()

        findViewById<View>(R.id.fabChat).setOnClickListener {
            showDialog("Chatbot", "Supervisor Assistant: How can I help with floor operations?")
        }
    }

    private fun setupWorkOrderCard() {
        val view = findViewById<View>(R.id.cardAssignedJobs)
        view.findViewById<TextView>(R.id.moduleTitle).text = "Work Orders"
        view.findViewById<TextView>(R.id.moduleDesc).text = "View assigned jobs and production status."

        view.findViewById<Button>(R.id.btn1).text = "View All Jobs"
        view.findViewById<Button>(R.id.btn2).text = "Pending Approvals"
        view.findViewById<Button>(R.id.btn3).text = "Update Status"
    }

    private fun setupMachineCard() {
        val view = findViewById<View>(R.id.cardMachineStatus)
        view.findViewById<TextView>(R.id.moduleTitle).text = "Machine Tracking"
        view.findViewById<TextView>(R.id.moduleDesc).text = "Live progress and health of floor machines."

        view.findViewById<Button>(R.id.btn1).text = "Live Telemetry"
        view.findViewById<Button>(R.id.btn2).text = "Maintenance Logs"
        view.findViewById<Button>(R.id.btn3).text = "Machine Health"
    }

    private fun setupAlertCard() {
        val view = findViewById<View>(R.id.cardAlerts)
        view.findViewById<TextView>(R.id.moduleTitle).text = "Issues & Alerts"
        view.findViewById<TextView>(R.id.moduleDesc).text = "Track production delays and problem alerts."

        view.findViewById<Button>(R.id.btn1).apply {
            text = "REPORT DELAY"
            setBackgroundColor(getColor(android.R.color.holo_orange_dark))
        }
        view.findViewById<Button>(R.id.btn2).text = "Safety Alerts"
        view.findViewById<Button>(R.id.btn3).text = "Critical Issues"
    }

    private fun showDialog(title: String, msg: String) {
        AlertDialog.Builder(this).setTitle(title).setMessage(msg).setPositiveButton("OK", null).show()
    }
}