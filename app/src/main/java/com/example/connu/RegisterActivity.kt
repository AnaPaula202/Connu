package com.example.connu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    private lateinit var etRName: EditText
    private lateinit var etRMail: EditText
    private lateinit var etRPass: EditText
    private lateinit var etRRepeatPass: EditText
    private lateinit var sRGender: Spinner
    private lateinit var bConfirmRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val tvGoback: TextView = findViewById(R.id.tvGoback)
        val ivGoback: ImageView = findViewById(R.id.ivGoback)

        tvGoback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        ivGoback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        etRName = findViewById(R.id.etRName)
        etRMail = findViewById(R.id.etRMail)
        etRPass = findViewById(R.id.etRPass)
        etRRepeatPass = findViewById(R.id.etRRepeatPass)
        sRGender = findViewById(R.id.sRGender)
        bConfirmRegister = findViewById(R.id.bConfirmRegister)


        // Configura el adapter para el spinner con las opciones de género
        val generoAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.gender_options,
            android.R.layout.simple_spinner_item
        )
        generoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sRGender.adapter = generoAdapter

        bConfirmRegister.setOnClickListener {
            validarYRegistrar()
        }
    }

    private fun validarYRegistrar() {
        val nombre: String = etRName.text.toString()
        val correo: String = etRMail.text.toString()
        val contrasena: String = etRPass.text.toString()
        val repetirContrasena: String = etRRepeatPass.text.toString()

        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || repetirContrasena.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Correo electrónico no válido", Toast.LENGTH_SHORT).show()
            return
        }

        if (contrasena != repetirContrasena) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtiene el índice seleccionado en el spinner
        val generoIndex: Int = sRGender.selectedItemPosition

        val genero: Int = when (generoIndex) {
            0 -> 1 // Femenino
            1 -> 2 // Masculino
            else -> 0 // Valor predeterminado si no hay selección válida
        }

        // Llama a la función para enviar los datos al servidor
        enviarDatos(nombre, correo, contrasena, genero)
    }

    private fun enviarDatos(
        name: String,
        mail: String,
        pass: String,
        sex: Int
    ) {
        val url = "http://192.168.1.67/connu/registrarUsuario.php";

        val requestQueue = Volley.newRequestQueue(this)
        val mapa = mutableMapOf<String, Any?>()

        mapa.put("name", name)
        mapa.put("mail", mail)

        mapa.put("pass", pass)
        mapa.put("sex", sex)

        val parametros : JSONObject = JSONObject(mapa)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            parametros,
            Response.Listener { response ->
                if(response.getBoolean("exito")){
                    finish()
                } else {
                    Toast.makeText(this, "Error en el servicio web", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Log.e("RegisterActivity", error.message.toString())
            }
        )

        requestQueue.add(request)
    }
}