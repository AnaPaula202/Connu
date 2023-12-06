package com.example.connu

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyPostViewHolder : RecyclerView.ViewHolder {

    lateinit var tvMyCuerpoPost : TextView
    lateinit var tvMyTipoPost : TextView
    lateinit var tvMyLikes : TextView
    lateinit var ivMyImagenPost : ImageView
    lateinit var bMyLike : ImageView
    lateinit var bDelete : Button
    constructor(itemView: View) : super(itemView){

        tvMyCuerpoPost = itemView.findViewById(R.id.tvMyPMessage)
        tvMyTipoPost = itemView.findViewById(R.id.tvMyPostType)
        tvMyLikes = itemView.findViewById(R.id.tvMyLikesAmount)
        ivMyImagenPost = itemView.findViewById(R.id.ivMyPImg)
        bMyLike = itemView.findViewById(R.id.bMyLike)
        bDelete = itemView.findViewById(R.id.bDeleteMyPost)

    }
}