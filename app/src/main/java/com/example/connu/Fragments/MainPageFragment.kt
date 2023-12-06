package com.example.connu.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.connu.Post
import com.example.connu.PostAdapter
import com.example.connu.R
import com.google.firebase.storage.FirebaseStorage
import org.json.JSONArray
import org.json.JSONObject


class MainPageFragment : Fragment() {
    private lateinit var sFilterMypage: Spinner
    private lateinit var adapterpost : PostAdapter
    private lateinit var storage : FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storage = FirebaseStorage.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_page, container, false)

        val view = inflater.inflate(R.layout.fragment_main_page, container, false)

        sFilterMypage = view.findViewById(R.id.sFilterMypage) // Reemplaza "your_spinner_id" con el ID de tu Spinner en el layout

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lista : RecyclerView = view.findViewById(R.id.posts)

        adapterpost = PostAdapter(this)

        lista.adapter = adapterpost

        val linearLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        lista.layoutManager = linearLayoutManager


        // Datos para el spinner de Tipo de publicación
        val tipoPublicacion = arrayOf("Tipo de publicación", "Postulación", "Contratación")

        val adapterTipoPublicacion = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            tipoPublicacion
        )
        adapterTipoPublicacion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sFilterMypage.adapter = adapterTipoPublicacion

        // Listener para el evento de cambio en el Spinner
        sFilterMypage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()

                // Lógica para enviar el índice seleccionado al método de consulta
                val indexToSend = when (selectedItem) {
                    "Tipo de publicación" -> "" // Carga todas las publicaciones
                    "Postulación" -> "1"
                    "Contratación" -> "2"
                    else -> ""
                }

                // Llamar a la consulta con el índice seleccionado o vacío para cargar todo
                consultarLista(indexToSend)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Si no se selecciona nada, cargar todas las publicaciones
                consultarLista("")
            }
        }
    }

    fun consultarLista(indexToSend: String) {
        val requestQueue = Volley.newRequestQueue(requireActivity())
        val url: String = "http://192.168.1.67/connu/listarPostsCategoria.php?index=$indexToSend"

        val request: JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener { response ->
                procesarLista(response)
            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    requireContext(),
                    "Error: $error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        requestQueue.add(request)
    }


    private fun procesarLista(response: JSONObject?) {
        if(response != null && response.getBoolean("exito")){
            val lista : JSONArray = response.getJSONArray("lista")
            val datos : ArrayList<Post> = ArrayList()

            for(i in 0 .. lista.length() - 1){
                val registro : JSONObject = lista.getJSONObject(i)

                val post = Post()

                post.user = registro.getString("nombre")
                post.idpost = registro.getInt("idpublicacion")
                post.content = registro.getString("contenido")
                post.img = registro.getString("img1")
                post.ptype = registro.getString("punombre")
                post.likes = registro.getString("likes")

                datos.add(post)
            }

            adapterpost.llenar(datos)
        }
    }

}