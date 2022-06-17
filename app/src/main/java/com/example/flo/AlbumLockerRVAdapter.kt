package com.example.flo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemLockerAlbumBinding
import com.example.flo.databinding.ItemStoreBinding

class AlbumLockerRVAdapter():
    RecyclerView.Adapter<AlbumLockerRVAdapter.ViewHolder>() {
    private val albums = ArrayList<Album>()

    interface MyItemClickListener {
        fun onRemoveSong(songId: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickLister: MyItemClickListener) {
        mItemClickListener = itemClickLister
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): AlbumLockerRVAdapter.ViewHolder {
        val binding: ItemLockerAlbumBinding =
            ItemLockerAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumLockerRVAdapter.ViewHolder, position: Int) {
        holder.bind(albums[position])
        holder.binding.itemLockerAlbumDeleteIv.setOnClickListener {
            mItemClickListener.onRemoveSong(albums[position].id)
            removeSong(position) //리사이클러뷰에서 지워줌
        }
    }

    override fun getItemCount(): Int = albums.size

    @SuppressLint("NotifyDataSetChanged")
    fun addAlbums(albums: ArrayList<Album>) { //SongDB에서 like값이 true인 것만
        this.albums.clear()
        this.albums.addAll(albums)

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeSong(position: Int) {
        albums.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemLockerAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            binding.itemLockerAlbumTitleTv.text = album.title
            binding.itemLockerAlbumSingerTv.text = album.singer
            binding.itemLockerAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }
    }
}