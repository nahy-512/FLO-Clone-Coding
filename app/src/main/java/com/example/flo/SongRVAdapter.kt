package com.example.flo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo.databinding.ItemSongBinding

class SongRVAdapter(val context: Context, val result : FloChartResult): RecyclerView.Adapter<SongRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SongRVAdapter.ViewHolder {
        val binding: ItemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongRVAdapter.ViewHolder, position: Int) {
        //holder.bind(result.songs[position])

        if(result.songs[position].coverImgUrl == "" || result.songs[position].coverImgUrl == null) {

        } else {
            Log.d("image", result.songs[position].coverImgUrl)
            Glide.with(context).load(result.songs[position].coverImgUrl).into(holder.coverImg) //커버 이미지가 null이 아니라면 받아온 cover에서 바인딩한 cover image에 적용시킴
        }
        holder.title.text = result.songs[position].title
        holder.singer.text = result.songs[position].singer
    }

    override fun getItemCount(): Int = result.songs.size

    inner class ViewHolder(val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root){

        val coverImg : ImageView = binding.itemSongCoverImgIv
        val title : TextView = binding.itemSongTitleTv
        val singer : TextView = binding.itemSongSingerTv
    }

    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }
}