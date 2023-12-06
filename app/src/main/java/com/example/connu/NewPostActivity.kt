package com.example.connu

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class NewPostActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var editText: EditText

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

        spinner = findViewById(R.id.spNewPType)
        editText = findViewById(R.id.etContent)

        val opciones: List<String> = when (tipoUsuario) {
            "General" -> listOf("Buscador de servicios")
            "Prestador" -> listOf("Buscador de servicios", "Proveedor de servicios")
            else -> emptyList()
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val newPostButton: Button = findViewById(R.id.bNewPost)
        newPostButton.setOnClickListener {
            val seleccion = spinner.selectedItem.toString()
            val opc = if (seleccion == "Proveedor de servicios") 2 else 1
            val content = editText.text.toString()

            if (content.isNotEmpty()) {
                nuevoPost(opc, idUsuario, content, "")
            } else {
                Toast.makeText(this, "El contenido no puede estar vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun nuevoPost(
        opc: Int,
        iduser: Int,
        content: String,
        img: String
    ) {
        val url = "http://192.168.1.67/connu/nuevoPost.php"

        val requestQueue = Volley.newRequestQueue(this)
        val mapa = mutableMapOf<String, Any?>()

        mapa.put("tipopost", opc)
        mapa.put("iduser", iduser)
        mapa.put("content", content)
        mapa.put("img", img)

        val parametros: JSONObject = JSONObject(mapa)

        val request: JsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            parametros,
            Response.Listener { response ->
                if (response.getBoolean("exito")) {
                    // Actualizar el fragmento o realizar cualquier acción necesaria
                    finish()
                } else {
                    Toast.makeText(this, "Error en el servicio web", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Log.e("NewPostActivity", "Error en la solicitud HTTP: ${error.message}")
            }
        )

        requestQueue.add(request)
    }
}
