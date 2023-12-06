package com.example.connu

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.connu.Fragments.MainPageFragment
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class PostAdapter(private val context: MainPageFragment) : RecyclerView.Adapter<PostViewHolder>() {
    private var datos: List<Post> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.post_fila, parent, false)

        return PostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datos.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = datos[position]

        holder.tvNombrePost.text = post.user
        holder.tvCuerpoPost.text = post.content
        holder.tvLikes.text = post.likes
        holder.tvTipoPost.text = post.ptype
        holder.tvContact.text = post.usermail

        holder.tvSex.text = if (post.usersex == 1) "Femenino" else "Masculino"

        if (post.img.isNotEmpty()) {
            // Concatenar la URL base de Firebase Storage con la ruta relativa
            val imageUrl = "https://firebasestorage.googleapis.com/v0/b/connu-87bc5.appspot.com/o/posts%2F318889-jugra.jpg?alt=media&token=86955617-f69f-45c0-977e-5455a3fa96bb"

            Picasso.get().load(imageUrl).into(holder.ivImagenPost, object : Callback {
                override fun onSuccess() {
                    holder.ivImagenPost.visibility = View.VISIBLE
                }

                override fun onError(e: Exception?) {
                    holder.ivImagenPost.visibility = View.GONE
                    Log.e("Picasso", "Error loading image", e)
                }
            })
        } else {
            holder.ivImagenPost.visibility = View.GONE
        }
    }

    fun llenar(datos: List<Post>) {
        this.datos = datos
        notifyDataSetChanged()
    }
}
