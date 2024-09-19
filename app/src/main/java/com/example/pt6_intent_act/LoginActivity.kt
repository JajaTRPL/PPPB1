package com.example.pt6_intent_act

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.PT6_intent_act.databinding.ActivityLoginBinding
import com.example.pt6_intent_act.MainActivity
import com.example.pt6_intent_act.ProfileActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataNama = intent.getStringExtra("EXTRA_NAMA")
        val dataEmail = intent.getStringExtra("EXTRA_EMAIL") ?: ""
        val dataNohp = intent.getStringExtra("EXTRA_NOHP")
        val dataJenisKelamin = intent.getStringExtra("EXTRA_JENISKELAMIN")
        val dataPassword = intent.getStringExtra("EXTRA_PASSWORD") ?: ""

        with(binding){
            txtSignup.setOnClickListener {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }
            buttonLogin.setOnClickListener {
                val email = inputEmail.text.toString()
                val password = inputPassword.text.toString()

                val userData = mapOf(
                    "Email" to email,
                    "Password" to password
                )
                var canSubmit = true
                userData.forEach { key, value ->
                    if(value.length == 0){
                        Toast.makeText(this@LoginActivity, key + " harus diisi!", Toast.LENGTH_SHORT).show()
                        canSubmit = false
                    }
                }
                if(canSubmit){
                    if((email == dataEmail) && (password == dataPassword)){
                        Toast.makeText(this@LoginActivity,"Login berhasil", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, ProfileActivity::class.java)
                        intent.putExtra("EXTRA_EMAIL", dataEmail)
                        intent.putExtra("EXTRA_NAMA", dataNama)
                        intent.putExtra("EXTRA_NOHP", dataNohp)
                        intent.putExtra("EXTRA_JENISKELAMIN", dataJenisKelamin)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@LoginActivity,"Email atau password salah!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}