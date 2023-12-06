package com.example.connu.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.connu.R
import org.json.JSONObject

class ProfileFragment : Fragment() {
    private lateinit var etNameProfile: EditText
    private lateinit var etMailProfile: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_profile, container, false)
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        etNameProfile = rootView.findViewById(R.id.etNameProfile)
        etMailProfile = rootView.findViewById(R.id.etMailProfile)

        // Obtener el idUsuario almacenado en SharedPreferences
        val sharedPreferences = activity?.getSharedPreferences("mi_pref", Context.MODE_PRIVATE)
        val idUsuario = sharedPreferences?.getInt("idUsuario", -1) ?: -1 // Obtener el idUsuario o -1 si no se encuentra

        // Hacer la solicitud JSON para obtener los datos del usuario según su id
        obtenerDatosUsuario(idUsuario)

        return rootView
    }

    private fun obtenerDatosUsuario(idUsuario: Int) {
        val url = "http://10.200.23.221/connu/obtenerUsuario.php?idUsuario=$idUsuario"

        val requestQueue = Volley.newRequestQueue(requireContext())

        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener { response ->
                // Procesar la respuesta JSON y actualizar los EditText con los datos del usuario
                val nombre = response.getString("nombre")
                val correo = response.getString("correo")

                etNameProfile.setText(nombre)
                etMailProfile.setText(correo)
            },
            Response.ErrorListener { error ->
                // Manejar errores de la solicitud si es necesario
            }
        )

        requestQueue.add(request)

    }


}