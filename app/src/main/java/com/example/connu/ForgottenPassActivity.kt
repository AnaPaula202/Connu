package com.example.connu

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ForgottenPassActivity : AppCompatActivity() {
    //private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var etRMail3: EditText
    private lateinit var bNewPass: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotten_pass)

        //--Componentes para regresar a las pestañas anteriores
        val tvGobackFP: TextView = findViewById(R.id.tvGobackFPass)
        val ivGobackFP: ImageView = findViewById(R.id.ivGobackFPass)

        //-- Etiqueta y botón para reestablecer contraseña
        val etRMail3 : EditText = findViewById(R.id.etRMail3)
        val bNewPass : Button = findViewById(R.id.bNewPass)

        tvGobackFP.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        ivGobackFP.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        bNewPass.setOnClickListener {
            //sendPasswordReset(etRMail3.text.toString())

        }
        //firebaseAuth = Firebase.auth
    }

    /*private fun sendPasswordReset(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(baseContext, "Correo Enviado", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(baseContext, "Error, no se pudo completar el proceso", Toast.LENGTH_SHORT).show()
                }

            }
}*/

}