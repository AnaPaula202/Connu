package com.example.connu

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var etLMail: EditText
    private lateinit var etLPass: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bRegister : Button = findViewById(R.id.bRegister)
        val bLogin : Button = findViewById(R.id.bLogin)
        val tvLostPass : TextView = findViewById(R.id.tvLostPass)

        bRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        tvLostPass.setOnClickListener {
            val intentf = Intent(this, ForgottenPassActivity::class.java)
            startActivity(intentf)
        }

        bLogin.setOnClickListener{

            etLMail = findViewById(R.id.etCorreo)
            etLPass = findViewById(R.id.etPass)

            val correo: String = etLMail.text.toString()
            val contrasena: String = etLPass.text.toString()

            val url = "http://10.200.23.221/connu/login.php";

            val requestQueue = Volley.newRequestQueue(this)
            val mapa = mutableMapOf<String, Any?>()

            mapa.put("mail", correo)
            mapa.put("pass", contrasena)

            val parametros : JSONObject = JSONObject(mapa)

            val request : JsonObjectRequest = JsonObjectRequest(
                Request.Method.POST,
                url,
                parametros,
                Response.Listener { response ->
                    if(response.getBoolean("exito")){

                        val sharedPreferences = getSharedPreferences("mi_pref", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()

                        // Obtener el idUsuario del JSON response y guardarlo en SharedPreferences
                        val idUsuario = response.getInt("idUsuario")
                        editor.putInt("idUsuario", idUsuario)
                        editor.apply()
                        val login = Intent(this, MainPageActivity::class.java)
                        startActivity(login)
                    } else {
                        Toast.makeText(this, "Error de credenciales", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("MainActivity", error.message.toString())
                }
            )

            requestQueue.add(request)
        }


    }

}