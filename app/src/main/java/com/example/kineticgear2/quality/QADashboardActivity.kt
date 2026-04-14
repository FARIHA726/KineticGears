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
import com.google.firebase.auth.FirebaseAuth

class QADashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qa_dashboard)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val btnMenu = findViewById<ImageView>(R.id.btnMenu)
        val tvLogout = findViewById<TextView>(R.id.tvLogout)

        btnMenu.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }

        tvLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
        }

        setupInspectionCard()
        setupApprovalCard()
        setupNCRCard()

        findViewById<View>(R.id.fabChat).setOnClickListener {
            showSimpleDialog("QA Assistant", "Hello! How can I help with quality audits today?")
        }
    }

    private fun setupInspectionCard() {
        val view = findViewById<View>(R.id.cardRecordInspection)
        view.findViewById<TextView>(R.id.moduleTitle).text = "Record Inspection"
        view.findViewById<TextView>(R.id.moduleDesc).text = "Enter Job ID, Inspector, and Remarks."

        view.findViewById<Button>(R.id.btn1).apply {
            text = "NEW INSPECTION"
            setTextColor(getColor(android.R.color.white))
            setBackgroundColor(getColor(android.R.color.holo_red_dark))
            setOnClickListener { showSimpleDialog("Camera", "Scanning Work Order QR...") }
        }
        view.findViewById<Button>(R.id.btn2).text = "ADD REMARKS"
        view.findViewById<Button>(R.id.btn3).text = "VIEW HISTORY"
    }

    private fun setupApprovalCard() {
        val view = findViewById<View>(R.id.cardApproval)
        view.findViewById<TextView>(R.id.moduleTitle).text = "Approval / Result"
        view.findViewById<TextView>(R.id.moduleDesc).text = "Review and finalize job status."

        view.findViewById<Button>(R.id.btn1).text = "APPROVE JOB"
        view.findViewById<Button>(R.id.btn2).text = "REJECT JOB"
        view.findViewById<Button>(R.id.btn3).text = "AUTO-UPDATE STATUS"
    }

    private fun setupNCRCard() {
        val view = findViewById<View>(R.id.cardNCR)
        view.findViewById<TextView>(R.id.moduleTitle).text = "NCR Linkage"
        view.findViewById<TextView>(R.id.moduleDesc).text = "Manage Non-Conformance Reports."

        view.findViewById<Button>(R.id.btn1).text = "CREATE NCR"
        view.findViewById<Button>(R.id.btn2).text = "VIEW LINKED NCR"
        view.findViewById<Button>(R.id.btn3).text = "MARK AS RESOLVED"
    }

    private fun showSimpleDialog(title: String, message: String) {
        AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton("OK", null).show()
    }
}