package com.example.flo.ui.main.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.entities.Album
import com.example.flo.databinding.ItemAlbumBinding

class AlbumRVAdapter(private val albumList: ArrayList<Album>): RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    //클릭 인터페이스 정의, 사용하고자 하는 함수 만들기
   interface MyItemClickListener {
        fun onItemClick(album: Album)
        fun onRemoveAlbum(position: Int)
    }

   //외부에서 전달받는 함수랑 전달받은 리스너 객체를 어댑터에서 사용할 수 있도록 따로 저장할 변수 선언
   private lateinit var mItemClickListener: MyItemClickListener
   fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
   }

    //아이템 추가
    fun addItem(album: Album) {
        albumList.add(album)
        notifyDataSetChanged()
    }

    //아이템 삭제
    fun removeItem(position: Int) {
        albumList.removeAt(position)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding) //재활용할 수 있게 뷰홀더에 아이템뷰 객체를 던져줌
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener{ mItemClickListener.onItemClick(albumList[position]) }
//        holder.binding.itemAlbumTitleTv.setOnClickListener { mItemClickListener.onRemoveAlbum(position) }
    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }

    }
}