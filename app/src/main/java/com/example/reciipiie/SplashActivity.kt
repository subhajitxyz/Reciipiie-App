package com.example.reciipiie

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            // User is signed in, navigate to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // No user is signed in, navigate to SignInActivity
            startActivity(Intent(this, SignInActivity::class.java))
        }
        finish()
    }
}
