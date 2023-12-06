package com.example.connu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.connu.Fragments.MainPageFragment
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

        holder.tvSex.text = if (post.usersex == 1) "Femenino" else "Masculino"

        if (post.img.isNotEmpty()) {
            Picasso.get().load(post.img).into(holder.ivImagenPost)
            holder.ivImagenPost.visibility = View.VISIBLE
        } else {
            holder.ivImagenPost.visibility = View.GONE
        }
    }

    fun llenar(datos: List<Post>) {
        this.datos = datos
        notifyDataSetChanged()
    }
}
