package com.example.connu

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        val cancelButton: Button = findViewById(R.id.bCancelNewPost)
        cancelButton.setOnClickListener {
            // Finalizar la actividad y regresar al Fragment
            finish()
        }

        val sharedPreferences =
            getSharedPreferences("mi_pref", Context.MODE_PRIVATE)
        val tipoUsuario = sharedPreferences.getString("tipoUsuario", "indefinido")
        val idUsuario = sharedPreferences.getInt("idUsuario", -1)

        val spinner: Spinner = findViewById(R.id.spNewPType)
        val editText: EditText = findViewById(R.id.etContent)

        val opciones: List<String> = when (tipoUsuario) {
            "General" -> listOf("Proveedor de servicios")
            "Prestador" -> listOf("Proveedor de servicios", "Buscador de servicios")
            else -> emptyList()
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun nuevoPost(
        name: String,
        mail: String,
        pass: String,
        sex: Int
    ) {
        val url = "http://192.168.1.67/connu/nuevoPost.php";

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