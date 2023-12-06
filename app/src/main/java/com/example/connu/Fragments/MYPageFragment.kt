package com.example.connu.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.connu.MyPost
import com.example.connu.MyPostAdapter
import com.example.connu.R
import com.google.firebase.storage.FirebaseStorage
import org.json.JSONArray
import org.json.JSONObject

class MYPageFragment : Fragment(), MyPostAdapter.OnDeleteClickListener {

    private lateinit var myadapterpost : MyPostAdapter
    private lateinit var storage : FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lista : RecyclerView = view.findViewById(R.id.rvMypage)

        myadapterpost = MyPostAdapter(this)
        lista.adapter = myadapterpost
        myadapterpost.setOnDeleteClickListener(this)

        val linearLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        lista.layoutManager = linearLayoutManager
        consultarMiLista()
    }

    override fun onDeleteClick(postId: Int) {
        val url = "http://192.168.1.67/connu/eliminarPost.php"

        val requestQueue = Volley.newRequestQueue(requireContext())
        val mapa = mutableMapOf<String, Any?>()

        mapa.put("idPost", postId)

        val parametros: JSONObject = JSONObject(mapa)

        val request: JsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            parametros,
            Response.Listener { response ->
                if (response.getBoolean("exito")) {
                    // La publicación se eliminó con éxito, puedes actualizar tu RecyclerView si es necesario
                    // También puedes mostrar un mensaje de éxito o realizar otras acciones
                    Toast.makeText(requireContext(), response.getString("msj"), Toast.LENGTH_SHORT).show()
                } else {
                    // Ocurrió un error al eliminar la publicación
                    Toast.makeText(requireContext(), "Error al eliminar la publicación", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Log.e("MYPageFragment", error.message.toString())
            }
        )

        requestQueue.add(request)

        myadapterpost.removeItem(postId)
    }


    fun consultarMiLista(){
        val sharedPreferences = requireContext().getSharedPreferences("mi_pref", Context.MODE_PRIVATE)
        val idUsuario = sharedPreferences.getInt("idUsuario", -1)

        val requestQueue = Volley.newRequestQueue(requireActivity())
        val url : String = "http://192.168.1.67/connu/listarMyPosts.php"

        val mapa = mutableMapOf<String, Any?>()

        mapa.put("idUsuario", idUsuario)

        val parametros: JSONObject = JSONObject(mapa)

        val request: JsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            parametros,
            Response.Listener { response ->
                if (response.getBoolean("exito")) {

                    procesarMiLista(response)
                } else {
                    //Toast.makeText(this, "Error de credenciales", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Log.e("MYPageFragment", error.message.toString())
            }
        )

        requestQueue.add(request)
    }

    private fun procesarMiLista(response: JSONObject?) {
        if(response != null && response.getBoolean("exito")){
            val lista : JSONArray = response.getJSONArray("lista")
            val datos : ArrayList<MyPost> = ArrayList()

            for(i in 0 .. lista.length() - 1){
                val registro : JSONObject = lista.getJSONObject(i)

                val post = MyPost()

                post.idmypost = registro.getInt("idpublicacion")
                post.mycontent = registro.getString("contenido")
                post.myimg = registro.getString("img1")
                post.myptype = registro.getString("punombre")
                post.mylikes = registro.getString("likes")

                datos.add(post)
            }

            myadapterpost.llenar(datos)
        }
    }


}