package com.example.connu

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostViewHolder : RecyclerView.ViewHolder {

    lateinit var tvNombrePost : TextView
    lateinit var tvCuerpoPost : TextView
    lateinit var tvTipoPost : TextView
    lateinit var tvLikes : TextView
    lateinit var ivImagenPost : ImageView
    lateinit var bLike : ImageView
    lateinit var tvSex : TextView

    lateinit var tvContact : TextView
    constructor(itemView: View) : super(itemView){

        tvNombrePost = itemView.findViewById(R.id.tvPName)
        tvCuerpoPost = itemView.findViewById(R.id.tvPMessage)
        tvTipoPost = itemView.findViewById(R.id.tvPostType)
        tvLikes = itemView.findViewById(R.id.tvLikesAmount)
        tvSex = itemView.findViewById(R.id.tvSexP)
        ivImagenPost = itemView.findViewById(R.id.ivPImg)
        bLike = itemView.findViewById(R.id.bLike)

        tvContact = itemView.findViewById(R.id.tvPostContact)

    }
}