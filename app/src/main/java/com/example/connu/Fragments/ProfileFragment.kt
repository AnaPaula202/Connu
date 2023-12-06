package com.example.connu.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.connu.Post
import com.example.connu.PostAdapter
import com.example.connu.R
import org.json.JSONArray
import org.json.JSONObject

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        consultarUser()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bChangeInfo: Button = requireView().findViewById(R.id.bChangeInfo)
        bChangeInfo.setOnClickListener {
            mostrarConfirmacion()
        }
    }

    fun consultarUser() {
        val sharedPreferences = requireContext().getSharedPreferences("mi_pref", Context.MODE_PRIVATE)
        val idUsuario = sharedPreferences.getInt("idUsuario", -1)

        val requestQueue = Volley.newRequestQueue(requireActivity())

        val url: String = "http://192.168.1.67/connu/visualizarUsuario.php?idUsuario=$idUsuario"

        val request: JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener { response ->
                Log.d("Response", response.toString())
                procesarUser(response)
            },
            Response.ErrorListener { error ->
                Log.e("Error", error.message.toString())
                //Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(request)
    }

    private fun procesarUser(response: JSONObject?) {
        if (response != null && response.getBoolean("exito")) {

            val sharedPreferences = requireContext().getSharedPreferences("mi_pref", Context.MODE_PRIVATE)
            val tipoUsuario = sharedPreferences.getString("tipoUsuario", "indefinido")

            // Obtener datos del usuario desde la respuesta JSON
            val nombreUsuario = response.getString("nombre")
            val correoUsuario = response.getString("correo")


            val etNombre: EditText = requireView().findViewById(R.id.etNameProfile)
            val etCorreo: EditText = requireView().findViewById(R.id.etMailProfile)
            val tvTipoUser : TextView = requireView().findViewById(R.id.tvPrType)

            etNombre.setText(nombreUsuario)
            etCorreo.setText(correoUsuario)
            tvTipoUser.text = tipoUsuario
        } else {

        }
    }

    private fun mostrarConfirmacion() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmación")
        builder.setMessage("¿Deseas actualizar la información?")

        builder.setPositiveButton("Sí") { _, _ ->
            actualizarInformacion()
        }

        builder.setNegativeButton("No") { _, _ ->

        }

        builder.show()
    }

    private fun actualizarInformacion() {
        val etNombre: EditText = requireView().findViewById(R.id.etNameProfile)
        val etCorreo: EditText = requireView().findViewById(R.id.etMailProfile)

        val nuevoNombre = etNombre.text.toString().trim()
        val nuevoCorreo = etCorreo.text.toString().trim()

        // Validaciones opcionales: asegúrate de que los nuevos datos no estén vacíos, etc.
        if (nuevoNombre.isEmpty() || nuevoCorreo.isEmpty()) {
            Toast.makeText(requireContext(), "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show()
            return
        }

        val idUsuario = obtenerIdUsuarioDesdeSharedPreferences()

        val url = "http://192.168.1.67/connu/actualizarUsuario.php";

        val requestQueue = Volley.newRequestQueue(requireContext())
        val mapa = mutableMapOf<String, Any?>()

        mapa.put("name", nuevoNombre)
        mapa.put("mail", nuevoCorreo)

        mapa.put("idUsuario", idUsuario)

        val parametros : JSONObject = JSONObject(mapa)

        val request : JsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            parametros,
            Response.Listener { response ->
                if(response.getBoolean("exito")){
                    Toast.makeText(requireContext(), "Información actualizada correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    //Toast.makeText(this, "Error en el servicio web", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Log.e("RegisterActivity", error.message.toString())
            }
        )

        requestQueue.add(request)
    }

    private fun obtenerIdUsuarioDesdeSharedPreferences(): Int {
        val sharedPreferences = requireContext().getSharedPreferences("mi_pref", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("idUsuario", -1)
    }


}