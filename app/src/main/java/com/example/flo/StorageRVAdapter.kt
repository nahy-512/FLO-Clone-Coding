package com.example.flo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemStoreBinding

class StorageRVAdapter():
    RecyclerView.Adapter<StorageRVAdapter.ViewHolder>(){
    private val songs = ArrayList<Song>()
    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickLister: MyItemClickListener){
        mItemClickListener = itemClickLister
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): StorageRVAdapter.ViewHolder {
        val binding: ItemStoreBinding = ItemStoreBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StorageRVAdapter.ViewHolder, position: Int) {
        holder.bind(songs[position])
        holder.binding.itemStoreDeleteIv.setOnClickListener {
            mItemClickListener.onRemoveSong(songs[position].id)
            removeSong(position) //리사이클러뷰에서 지워줌
        }
    }

    override fun getItemCount(): Int = songs.size

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) { //SongDB에서 like값이 true인 것만
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeSong(position: Int) {
        songs.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemStoreBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song ) {
            binding.itemStoreTitleTv.text = song.title
            binding.itemStoreSingerTv.text = song.singer
            binding.itemStoreCoverImgIv.setImageResource(song.coverImg!!)
        }
    }

}