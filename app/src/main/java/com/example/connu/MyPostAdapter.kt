package com.example.connu

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.connu.Fragments.MYPageFragment
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class MyPostAdapter (private val context: MYPageFragment) : RecyclerView.Adapter<MyPostViewHolder>() {
    private var datos: List<MyPost> = ArrayList()
    private var onDeleteClickListener: OnDeleteClickListener? = null


    fun removeItem(position: Int) {
        datos.toMutableList().removeAt(position)
        notifyDataSetChanged()
    }
    interface OnDeleteClickListener {
        fun onDeleteClick(postId: Int)
    }

    fun setOnDeleteClickListener(listener: MYPageFragment) {
        onDeleteClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPostViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.my_post_fila, parent, false)

        return MyPostViewHolder(view)
    }


    override fun getItemCount(): Int {
        return datos.size
    }

    override fun onBindViewHolder(holder: MyPostViewHolder, position: Int) {
        val post = datos[position]

        holder.tvMyCuerpoPost.text = post.mycontent
        holder.tvMyLikes.text = post.mylikes
        holder.tvMyTipoPost.text = post.myptype

        if (post.myimg.isNotEmpty()) {
            // Concatenar la URL base de Firebase Storage con la ruta relativa
            val imageUrl = "https://firebasestorage.googleapis.com/v0/b/connu-87bc5.appspot.com/o/posts%2F318889-jugra.jpg?alt=media&token=86955617-f69f-45c0-977e-5455a3fa96bb"

            Picasso.get().load(imageUrl).into(holder.ivMyImagenPost, object : Callback {
                override fun onSuccess() {
                    holder.ivMyImagenPost.visibility = View.VISIBLE
                }

                override fun onError(e: Exception?) {
                    holder.ivMyImagenPost.visibility = View.GONE
                    Log.e("Picasso", "Error loading image", e)
                }
            })
        } else {
            holder.ivMyImagenPost.visibility = View.GONE
        }

        holder.bDelete.setOnClickListener {
            onDeleteClickListener?.onDeleteClick(datos[position].idmypost)
        }

    }


    fun llenar(datos: List<MyPost>) {
        this.datos = datos
        notifyDataSetChanged()
    }
}